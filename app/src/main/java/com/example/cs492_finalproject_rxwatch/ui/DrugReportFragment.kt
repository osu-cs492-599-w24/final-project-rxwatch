package com.example.cs492_finalproject_rxwatch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.DrugInfo
import com.example.cs492_finalproject_rxwatch.data.DrugInformation

class DrugReportFragment : Fragment(R.layout.drug_report_fragment) {
    private val viewModel: DrugInformationViewModel by viewModels()
    private val adapter = DrugInteractionsAdapter(::onDrugInfoItemClick)

    private lateinit var searchResultsListRV: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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
            Log.d("DrugReportFragment", "Search Results: $searchResults")
            if (searchResults != null) {
//                adapter.updateDrugInteractionsList(searchResults)
                searchResultsListRV.visibility = View.VISIBLE
                searchResultsListRV.scrollToPosition(0)
            }

        }
    }

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
}