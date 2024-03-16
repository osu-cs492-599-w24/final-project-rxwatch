package com.example.cs492_finalproject_rxwatch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs492_finalproject_rxwatch.data.AdverseEventsRepository
import com.example.cs492_finalproject_rxwatch.data.DrugInfoService
import kotlinx.coroutines.launch

class AdverseEventsViewModel: ViewModel() {
    private val repository = AdverseEventsRepository(DrugInfoService.create())

    fun loadAdverseEvents(search: String) {
        viewModelScope.launch {
            val result = repository.getAdverseEvents(search)
        }
    }

    fun loadReactionOutcomeCount(search: String) {
        viewModelScope.launch {
            val result = repository.getReactionOutcomeCount(search)
        }
    }
}