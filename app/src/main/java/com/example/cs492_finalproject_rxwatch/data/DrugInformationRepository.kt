package com.example.cs492_finalproject_rxwatch.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class DrugInformationRepository(
    private val service: DrugInfoService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var lastDrugSearched: String? = null

    private var cachedDrugInformation: DrugInformation? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timestamp = timeSource.markNow()

    suspend fun getDrugInformation(search: String): Result<DrugInformation?> {
        Log.d("DrugInformationRepository", "Search Query: $search")
        return if(shouldFetchOutcomeCount(search)) {
            withContext(ioDispatcher) {
                try {
                    val response = service.getDrugInformation(search)
                    Log.d("DrugInformationRepository", "Response: $response")
                    if(response.isSuccessful) {
                        cachedDrugInformation = response.body()
                        timestamp = timeSource.markNow()
                        lastDrugSearched = search
                        Result.success(cachedDrugInformation)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch(e: Exception) {
                    Result.failure(e)
                }
            }
        } else {
            Result.success(cachedDrugInformation!!)
        }
    }

    private fun shouldFetchOutcomeCount(search: String): Boolean =
        cachedDrugInformation == null
                || !search.contains(lastDrugSearched.toString(), true)
                || (timestamp + cacheMaxAge).hasPassedNow()

}