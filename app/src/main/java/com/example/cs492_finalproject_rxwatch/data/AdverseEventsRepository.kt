package com.example.cs492_finalproject_rxwatch.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdverseEventsRepository(
    private val service: DrugInfoService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var cachedAdverseEvents: AdverseEvents? = null

    suspend fun getAdverseEvents(search: String): Result<AdverseEvents?> {
        return withContext(ioDispatcher) {
            val response = service.getAdverseEvents(search)

            Result.success(response.body())
        }
    }
}