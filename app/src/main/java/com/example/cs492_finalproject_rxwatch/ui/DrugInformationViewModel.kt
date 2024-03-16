package com.example.cs492_finalproject_rxwatch.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs492_finalproject_rxwatch.data.DrugInfoService
import com.example.cs492_finalproject_rxwatch.data.DrugInformation
import com.example.cs492_finalproject_rxwatch.data.DrugInformationRepository
import kotlinx.coroutines.launch
import kotlin.math.log

class DrugInformationViewModel: ViewModel() {
    private val repository = DrugInformationRepository(DrugInfoService.create())

    private val _searchResults = MutableLiveData<List<DrugInformation>?>(null)
    val searchResults: LiveData<List<DrugInformation>?> = _searchResults

    fun loadDrugInteractions(search: String) {
        Log.d("DrugInformationViewModel", "Search query: $search")
        viewModelScope.launch {
            val result = repository.getDrugInformation(search)
            Log.d("DrugInformationViewModel", "Search Results: $result")
        }
    }
}