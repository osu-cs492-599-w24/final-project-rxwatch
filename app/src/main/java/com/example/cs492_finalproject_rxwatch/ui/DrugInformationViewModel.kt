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

/**
 * ViewModel for the DrugInformationFragment
 * @property repository DrugInformationRepository
 * @property searchResults LiveData<DrugInformation?>
 *     The search results for the drug interaction search
 *     This is a LiveData object that will be updated when the search results are updated
 *
 * @property loading LiveData<Boolean>
 *     A LiveData object that will be updated when the search is loading
 *
 * @property error LiveData<String?>
 *     A LiveData object that will be updated when there is an error with the search
 *
 * @constructor Creates a DrugInformationViewModel
 */
class DrugInformationViewModel: ViewModel() {
    // Create a DrugInformationRepository
    private val repository = DrugInformationRepository(DrugInfoService.create())

    // Create LiveData objects for the search results, loading, and error
    private val _searchResults = MutableLiveData<DrugInformation?>(null)
    val searchResults: LiveData<DrugInformation?> = _searchResults

    // Create LiveData objects for the loading and error
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    // Create LiveData objects for the error
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    /**
     * Load the drug interactions for the given search query
     * @param search String
     *     The search query to use to search for drug interactions
     *     This will be the drug name
     *     Example query:
     *     https://api.fda.gov/drug/label.json?search=(drug_interactions:($searchedDrugName))+(_exists_:openfda.generic_name)
     *
     *    The API documentation says to uses `+AND+` to search multiple fields
     *    However, Retrofit appears to be URL encoding the `+` signs, so it gets double encoded.
     *    Replacing the `+` signs with a space ` ` appears to fix the issue.
     *
     *    The search query will be used to search the drug label endpoint for the drug interactions
     */
    fun loadDrugInteractions(search: String) {
        Log.d("DrugInformationViewModel", "Search query: $search")
        // Use the viewModelScope to launch a coroutine to get the drug information
        viewModelScope.launch {
            _loading.value = true
            // Get the drug information from the repository
            val result = repository.getDrugInformation(search)
            _loading.value = false
            Log.d("DrugInformationViewModel", "Search Results: $result")
            // Update the search results, loading, and error LiveData objects
            _searchResults.value = result.getOrNull() // Get the search results
            _error.value = result.exceptionOrNull()?.message // Get the error message
        }
    }
}