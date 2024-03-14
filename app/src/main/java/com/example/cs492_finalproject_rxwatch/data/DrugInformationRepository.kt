package com.example.cs492_finalproject_rxwatch.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrugInformationRepository(
    private val service: DrugInfoService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var cachedDrugInformation: DrugInformation? = null

    suspend fun getDrugInformation(search: String): Result<DrugInformation?> {
        return withContext(ioDispatcher) {
            val response = service.getDrugInformation(search)

            Result.success(response.body())
        }
    }
}