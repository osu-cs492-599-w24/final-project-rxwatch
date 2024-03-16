package com.example.cs492_finalproject_rxwatch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs492_finalproject_rxwatch.data.AdverseEvents
import com.example.cs492_finalproject_rxwatch.data.AdverseEventsRepository
import com.example.cs492_finalproject_rxwatch.data.DrugInfoService
import com.example.cs492_finalproject_rxwatch.data.Outcomes
import kotlinx.coroutines.launch

class AdverseEventsViewModel: ViewModel() {
    private val repository = AdverseEventsRepository(DrugInfoService.create())

    private val _adverseEvents = MutableLiveData<AdverseEvents?>(null)
    val adverseEvents: LiveData<AdverseEvents?> = _adverseEvents

    private val _outcomeCounts = MutableLiveData<Outcomes?>(null)
    val outcomeCounts: LiveData<Outcomes?> = _outcomeCounts

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    fun loadAdverseEvents(search: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getAdverseEvents(search)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _adverseEvents.value = result.getOrNull()
        }
    }

    fun loadReactionOutcomeCount(search: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getReactionOutcomeCount(search)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _outcomeCounts.value = result.getOrNull()
        }
    }
}