package com.example.cs492_finalproject_rxwatch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cs492_finalproject_rxwatch.R

class DrugReportFragment : Fragment(R.layout.drug_report_fragment) {
    private val viewModel: DrugInformationViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: Implement Drug Report Screen
        Log.d("DrugReportFragment", "View: $view")
        val exampleDrug = "hydrocodone"

        /*
        * The API documentation says to uses `+AND+` to search multiple fields
        * However, Retrofit appears to be URL encoding the `+` signs, so it gets double encoded.
        * Replacing the `+` signs with a space ` ` appears to fix the issue.
        * */
        val exampleQuery = "drug_interactions:$exampleDrug AND _exists_:openfda.generic_name"
        viewModel.loadDrugInteractions(exampleQuery)
    }
}