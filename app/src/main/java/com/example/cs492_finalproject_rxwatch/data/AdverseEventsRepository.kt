package com.example.cs492_finalproject_rxwatch.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class AdverseEventsRepository(
    private val service: DrugInfoService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var lastDrugName: String? = null

    private var cachedOutcomeCounts: Outcomes? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timestamp = timeSource.markNow()

    suspend fun getReactionOutcomeCount(search: String): Result<Outcomes?> {
        return if(shouldFetchOutcomeCount(search)) {
            withContext(ioDispatcher) {
                try{
                    val response = service.getReactionOutcomeCount(search)
                    if(response.isSuccessful) {
                        cachedOutcomeCounts = response.body()
                        timestamp = timeSource.markNow()
                        lastDrugName = "test"
                        Result.success(cachedOutcomeCounts)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch(e: Exception) {
                    Result.failure(e)
                }
            }
        } else {
            Result.success(cachedOutcomeCounts!!)
        }
    }

    private fun shouldFetchOutcomeCount(search: String): Boolean =
        cachedOutcomeCounts == null
                || !search.contains(lastDrugName.toString(), true)
                || (timestamp + cacheMaxAge).hasPassedNow()
}