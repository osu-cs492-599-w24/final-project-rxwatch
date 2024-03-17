package com.example.cs492_finalproject_rxwatch.data.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SearchedDrugViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SearchedDrugRepository(
        SearchedDrugDatabase.getInstance(application).searchedDrugDao()
    )

    val searchedDrugs = repository.getAllSearchedDrugs().asLiveData()

    fun deleteDrugByName(name: String) {
        viewModelScope.launch {
            repository.deleteSearchedDrugByName(name)
        }
    }

    fun addSearchedDrug(drug: SearchedDrug) {
        viewModelScope.launch {
            repository.insertSearchedDrug(drug)
        }
    }

    fun removeSearchedDrug(drug: SearchedDrug) {
        viewModelScope.launch {
            repository.deleteSearchedDrug(drug)
        }
    }
}