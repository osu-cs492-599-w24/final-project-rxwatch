package com.example.cs492_finalproject_rxwatch.data

import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface DrugInfoService {
    @GET("label.json")
    suspend fun getDrugInformation(
        @Query("search") search: String,
        @Query("limit") limit: Int = 25
    ): Response<DrugInformation>

    @GET("event.json")
    suspend fun getAdverseEvents(
        @Query("search") search: String,
        @Query("limit") limit: Int = 25
    ): Response<AdverseEvents>

    companion object {
        private const val BASE_URL = "https://api.fda.gov/drug/"

        fun create(): DrugInfoService {
            val moshi = Moshi.Builder().build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(DrugInfoService::class.java)
        }
    }
}