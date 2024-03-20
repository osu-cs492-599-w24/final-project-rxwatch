package com.example.cs492_finalproject_rxwatch.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.database.SearchedDrugViewModel
import com.example.cs492_finalproject_rxwatch.utils.OutcomesEnum
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.progressindicator.CircularProgressIndicator

/**
 * A simple [Fragment] subclass.
 * Use the [AdverseEventsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * This fragment is responsible for displaying the adverse events
 * for a given drug. It uses the OpenFDA API to get the data and
 * displays it in a pie chart.
 */
class AdverseEventsFragment : Fragment(R.layout.adverse_events_layout) {
    //View model for the adverse events fragment
    private val viewModel: AdverseEventsViewModel by viewModels()

    //View model for the searched drug. Used to get the most recent searched drug to use in the API call
    private val searchedDrugsViewModel: SearchedDrugViewModel by viewModels()

    //Total number of outcomes for the drug
    private var totalOutcomes: Int = 0
    private var outcomeCount = mutableMapOf<String, Int>()

    //Pie chart for displaying the data
    private lateinit var pieChart: PieChart

    //Loading indicator for when the API call is being made
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var adverseEventsView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adverseEventsView = view.findViewById(R.id.constraint_layout_adverse)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        /*
         * Whenever a API call is made through the view model that doesn't
         * used cache data the outcomeCounts will be changed calling this
         * listener. This function maintains the data set for our pie chart
         * and sets it up with the values we have stored.
         */
        viewModel.outcomeCounts.observe(viewLifecycleOwner) { outcomes ->
            if (outcomes != null) {
                Log.d("AdverseEventsFragment", outcomes.toString())

                adverseEventsView.visibility = View.VISIBLE

                pieChart = view.findViewById(R.id.pieChart_view)

                val label = "Type"
                val pieDataset: PieDataSet
                val pieEntries = mutableListOf<PieEntry>()
                val pieColors = mutableListOf<Int>()

                totalOutcomes = 0

                outcomes.results.forEach { count ->
                    totalOutcomes += count.count

                    //Map the 6 values into the only 4 that we want using enum
                    when(count.term) {
                        OutcomesEnum.NOT_RECOVERED_OR_RESOLVED.value,
                        OutcomesEnum.RECOVERING_RESOLVING.value,
                        OutcomesEnum.RECOVERED_RESOLVED.value -> {
                            if (outcomeCount.containsKey("Hospitilization")) {
                                outcomeCount["Hospitilization"] =
                                    outcomeCount["Hospitilization"]!! + count.count
                            } else {
                                outcomeCount["Hosipitilization"] = count.count
                            }
                        }

                        OutcomesEnum.RECOVERED_WITH_LONG_TERM_ISSUES.value -> {
                            outcomeCount["Long Lasting Effects"] = count.count
                        }

                        OutcomesEnum.FATAL.value -> {
                            outcomeCount["Death"] = count.count
                        }

                        OutcomesEnum.UNKNOWN.value -> {
                            outcomeCount["Other"] = count.count
                        }
                    }
                }

                //add colors for pie chart
                pieColors.add(Color.parseColor("#5e81ac"));
                pieColors.add(Color.parseColor("#a3be8c"));
                pieColors.add(Color.parseColor("#b48ead"));
                pieColors.add(Color.parseColor("#ebcb8b"));

                //populating pie chart with data with data we just stored in the
                // outcomeCount map
                outcomeCount.keys.forEach { key ->
                    pieEntries.add(0, PieEntry(outcomeCount[key]?.toFloat() ?: 0f, key))
                }

                //set data and colors
                pieDataset = PieDataSet(pieEntries, label)
                pieDataset.colors = pieColors

                //create pie data with our data set and set
                //other values
                val pieData = PieData(pieDataset)
                pieData.setDrawValues(true)
                pieData.setValueTextSize(18f)
                pieData.setValueFormatter(PercentFormatter())

                //initialization and customization for pie chart
                pieChart.data = pieData
                pieChart.setUsePercentValues(true)
                pieChart.isRotationEnabled = true
                pieChart.dragDecelerationFrictionCoef = 0.9f
                pieChart.rotationAngle = 0f
                pieChart.holeRadius = 0f
                pieChart.transparentCircleRadius = 0f
                pieChart.description.isEnabled = false
                pieChart.invalidate()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Log.e("AdverseEventsFragment", "Error fetching forecast: ${error.message}")
                error.printStackTrace()
            }
        }

        //Set up an observer for the loading status of the API query
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                adverseEventsView.visibility = View.INVISIBLE
                loadingIndicator.visibility = View.VISIBLE
            }
            else{
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

    }

    override fun onResume() {
        super.onResume()

        searchedDrugsViewModel.mostRecentSearchedDrug.observe(viewLifecycleOwner) { drug ->
            if (drug != null) {
                val query = "patient.drug.openfda.generic_name:${drug[0].drugName}"
                viewModel.loadReactionOutcomeCount(query)
            }
        }

//        val exampleDrug = "ibuprofen"
//
//        val exampleQuery = "patient.drug.openfda.generic_name:$exampleDrug"
//        viewModel.loadReactionOutcomeCount(exampleQuery)
    }
}