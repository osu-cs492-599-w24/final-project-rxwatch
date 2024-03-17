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

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            if (searchResults != null) {
                Log.d("DrugReportFragment", "Search Results: $searchResults")
                Log.d("DrugReportFragment", "Search Results List of DrugInfo: ${searchResults.results}")

                adapter.updateDrugInteractionsList(searchResults.results)

                searchResultsListRV.visibility = View.VISIBLE
                searchResultsListRV.scrollToPosition(0)

                // Prep and share the list of drugs
                shareButton.setOnClickListener {
                    val shareText = buildDrugListShareString(exampleDrug, searchResults.results)
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

    private fun onDrugInfoItemClick(drugInfo: DrugInformation) {
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