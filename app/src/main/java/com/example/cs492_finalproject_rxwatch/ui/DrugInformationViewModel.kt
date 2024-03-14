package com.example.cs492_finalproject_rxwatch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs492_finalproject_rxwatch.data.DrugInfoService
import com.example.cs492_finalproject_rxwatch.data.DrugInformationRepository
import kotlinx.coroutines.launch

class DrugInformationViewModel: ViewModel() {
    private val repository = DrugInformationRepository(DrugInfoService.create())

    fun loadAdverseEvents(search: String) {
        viewModelScope.launch {
            val result = repository.getDrugInformation(search)
        }
    }
}