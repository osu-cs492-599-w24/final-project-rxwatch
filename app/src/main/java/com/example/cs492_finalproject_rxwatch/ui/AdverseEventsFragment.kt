package com.example.cs492_finalproject_rxwatch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.utils.OutcomesEnum

class AdverseEventsFragment : Fragment(R.layout.adverse_events_layout) {
    private val viewModel: AdverseEventsViewModel by viewModels()

    private var totalOutcomes: Int = 0
    private var outcomeCount = mutableMapOf<Int, Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.outcomeCounts.observe(viewLifecycleOwner) { outcomes ->
            if (outcomes != null) {
                Log.d("AdverseEventsFragment", outcomes.toString())

                totalOutcomes = 0

                outcomes.results.forEach { count ->
                    totalOutcomes += count.count

                    when(count.term) {
                        OutcomesEnum.NOT_RECOVERED_OR_RESOLVED.num,
                        OutcomesEnum.RECOVERING_RESOLVING.num,
                        OutcomesEnum.RECOVERED_RESOLVED.num -> {
                            if (outcomeCount.containsKey(0)) {
                                outcomeCount[0] = outcomeCount[0]!! + count.count
                            } else {
                                outcomeCount[0] = count.count
                            }
                        }

                        OutcomesEnum.RECOVERED_WITH_LONG_TERM_ISSUES.num -> {
                            outcomeCount[1] = count.count
                        }

                        OutcomesEnum.FATAL.num -> {
                            outcomeCount[2] = count.count
                        }

                        OutcomesEnum.UNKNOWN.num -> {
                            outcomeCount[3] = count.count
                        }
                    }
                }

                Log.d("AdverseEventsFragment", outcomeCount.toString())
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

        val exampleDrug = "hydrocodone"

        val exampleQuery = "patient.drug.openfda.generic_name:$exampleDrug"
        viewModel.loadReactionOutcomeCount(exampleQuery)
    }
}