package com.example.cs492_finalproject_rxwatch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.DrugInfo
import com.example.cs492_finalproject_rxwatch.data.DrugInformation
import com.example.cs492_finalproject_rxwatch.data.DrugInteractionsDisplay
import com.example.cs492_finalproject_rxwatch.data.Manufacturers

class DrugReportFragment : Fragment(R.layout.drug_report_fragment) {
    private val viewModel: DrugInformationViewModel by viewModels()
    private val adapter = DrugInteractionsAdapter(::onDrugInfoItemClick)

    private lateinit var searchResultsListRV: RecyclerView

    private lateinit var adverseButton: Button
    private lateinit var shareButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adverseButton = view.findViewById(R.id.btn_navigate_to_adverse)
        shareButton = view.findViewById(R.id.btn_share_interactions)

        adverseButton.setOnClickListener {
            val directions = DrugReportFragmentDirections.navigateToAdverseEvents()
            findNavController().navigate(directions)
        }

        /*
        * The API documentation says to uses `+AND+` to search multiple fields
        * However, Retrofit appears to be URL encoding the `+` signs, so it gets double encoded.
        * Replacing the `+` signs with a space ` ` appears to fix the issue.
        * */
        val exampleDrug = "hydrocodone"
        val exampleQuery = "drug_interactions:$exampleDrug AND _exists_:openfda.generic_name"
        viewModel.loadDrugInteractions(exampleQuery)

        searchResultsListRV = view.findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(requireContext())
        searchResultsListRV.setHasFixedSize(true)
        searchResultsListRV.adapter = adapter

        viewModel.searchResults.observe(viewLifecycleOwner) { drugInformationResults ->
            if (drugInformationResults != null) {
                Log.d("DrugReportFragment", "Search Results: $drugInformationResults")
                Log.d("DrugReportFragment", "Search Results List of DrugInfo: ${drugInformationResults.results}")

                val drugInfoList = drugInformationResults.results

                /*
                * Build a map of the manufacturer (mfr) drug names to the drug interactions
                * and nest it within a map of the generic drug names.
                * This helps condense the search results, since many manufacturers make the same drug.
                * */
                val drugInteractionsMap = mutableMapOf<String, MutableMap<String, String>>()

                drugInfoList.forEach { drugInfo ->
                    drugInfo.openFDA?.genericName?.forEach{ genericName ->
                        val mfrToInteractionsMap = drugInteractionsMap.getOrPut(genericName) {
                            mutableMapOf()
                        }
                        drugInfo.openFDA.manufacturerName?.forEach { mfrName ->
                            val interactionsString = drugInfo.drugInteractionsString?.joinToString(
                                separator = "; ")
                                ?: "No interactions found"
                            mfrToInteractionsMap[mfrName] = interactionsString
                        }
                    }
                }

                // ****** Logging logic for debugging and not necessary for app functionality ******
                Log.d("DrugReportFragment", "drugInteractionsMap: $drugInteractionsMap")
                var totalMfrCount = 0
                drugInteractionsMap.forEach { (_, mfrMap) ->
                    totalMfrCount += mfrMap.size
                }
                val genericCount = drugInteractionsMap.size
                val avgMfrsPerGeneric = if (genericCount > 0 ) totalMfrCount.toDouble() / genericCount else 0.0
                Log.d("DrugReportFragment", "Total Generic Names: $genericCount")
                Log.d("DrugReportFragment", "Total Mfr Names: $totalMfrCount")
                Log.d("DrugReportFragment", "Avg Mfr name per Generic Name: $avgMfrsPerGeneric")
                // *********************************************************************************

                // Convert the nested map into nested data classes
                // so the adapter can more easily work with it
                val displayList = drugInteractionsMap.map { (genericName, mfrMap) ->
                    DrugInteractionsDisplay(
                        genericName = genericName,
                        manufacturerName = mfrMap.map { (mfrName, interactions) ->
                            Manufacturers(mfrName, interactions)
                        }
                    )
                }

                // Send the new nested data classes to the adapter
                adapter.updateDrugInteractionsList(displayList)

                searchResultsListRV.visibility = View.VISIBLE
                searchResultsListRV.scrollToPosition(0)

                // Prep and share the list of drugs
                shareButton.setOnClickListener {
                    val shareText = buildDrugListShareString(exampleDrug, drugInfoList)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    startActivity(Intent.createChooser(intent, null))
                }
            }
        }
    }

    // TODO Implement the onResume() method
//    override fun onResume() {
//        super.onResume()
//
//        val exampleDrug = "hydrocodone"
//        val exampleQuery = "drug_interactions:$exampleDrug AND _exists_:openfda.generic_name"
//        viewModel.loadDrugInteractions(exampleQuery)
//    }

    private fun onDrugInfoItemClick(drugInfo: DrugInteractionsDisplay) {
        Log.d("DrugReportFragment", "Item clicked: $drugInfo")
    }


    /*
    * Takes a list of DrugInfo objects from the API results and builds a string from them
    * that can be incorporated in a ShareSheet.
    * */
    private fun buildDrugListShareString(searchedDrug: String, drugList: List<DrugInfo>) : String {
        var interactionsString: String = ""
        for(drug in drugList){
            val drugName = drug.openFDA?.genericName
            interactionsString += "${drugName}, "
        }
        return getString(R.string.share_text, searchedDrug, interactionsString.dropLast(2))
    }
}