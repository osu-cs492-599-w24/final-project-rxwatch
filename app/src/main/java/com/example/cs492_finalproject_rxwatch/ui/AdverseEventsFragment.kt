package com.example.cs492_finalproject_rxwatch.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.utils.OutcomesEnum
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter


class AdverseEventsFragment : Fragment(R.layout.adverse_events_layout) {
    private val viewModel: AdverseEventsViewModel by viewModels()

    private var totalOutcomes: Int = 0
    private var outcomeCount = mutableMapOf<String, Int>()

    private lateinit var pieChart: PieChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.outcomeCounts.observe(viewLifecycleOwner) { outcomes ->
            if (outcomes != null) {
                Log.d("AdverseEventsFragment", outcomes.toString())

                pieChart = view.findViewById(R.id.pieChart_view)

                val label = "Type"
                val pieDataset: PieDataSet
                val pieEntries = mutableListOf<PieEntry>()
                val pieColors = mutableListOf<Int>()

                totalOutcomes = 0

                outcomes.results.forEach { count ->
                    totalOutcomes += count.count

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

                pieColors.add(Color.parseColor("#5e81ac"));
                pieColors.add(Color.parseColor("#a3be8c"));
                pieColors.add(Color.parseColor("#b48ead"));
                pieColors.add(Color.parseColor("#ebcb8b"));

                outcomeCount.keys.forEach { key ->
                    pieEntries.add(0, PieEntry(outcomeCount[key]?.toFloat() ?: 0f, key))
                }

                pieDataset = PieDataSet(pieEntries, label)
                pieDataset.colors = pieColors

                val pieData = PieData(pieDataset)
                pieData.setDrawValues(true)
                pieData.setValueTextSize(18f)
                pieData.setValueFormatter(PercentFormatter())

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
    }

    override fun onResume() {
        super.onResume()

        val exampleDrug = "ibuprofen"

        val exampleQuery = "patient.drug.openfda.generic_name:$exampleDrug"
        viewModel.loadReactionOutcomeCount(exampleQuery)
    }
}