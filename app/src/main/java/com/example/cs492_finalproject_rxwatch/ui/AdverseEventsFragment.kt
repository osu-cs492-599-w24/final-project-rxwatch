package com.example.cs492_finalproject_rxwatch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.utils.OutcomesEnum
import com.example.cs492_finalproject_rxwatch.utils.OutcomesSortedEnum

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
                        OutcomesEnum.NOT_RECOVERED_OR_RESOLVED.value,
                        OutcomesEnum.RECOVERING_RESOLVING.value,
                        OutcomesEnum.RECOVERED_RESOLVED.value -> {
                            if (outcomeCount.containsKey(OutcomesSortedEnum.HOSPITILIZATION.value)) {
                                outcomeCount[OutcomesSortedEnum.HOSPITILIZATION.value] =
                                    outcomeCount[OutcomesSortedEnum.HOSPITILIZATION.value]!! + count.count
                            } else {
                                outcomeCount[OutcomesSortedEnum.HOSPITILIZATION.value] = count.count
                            }
                        }

                        OutcomesEnum.RECOVERED_WITH_LONG_TERM_ISSUES.value -> {
                            outcomeCount[OutcomesSortedEnum.LONG_LASTING_EFFECTS.value] = count.count
                        }

                        OutcomesEnum.FATAL.value -> {
                            outcomeCount[OutcomesSortedEnum.DEATH.value] = count.count
                        }

                        OutcomesEnum.UNKNOWN.value -> {
                            outcomeCount[OutcomesSortedEnum.OTHER.value] = count.count
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