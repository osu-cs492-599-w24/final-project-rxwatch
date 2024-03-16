package com.example.cs492_finalproject_rxwatch.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AdverseEvents(
    @Json(name = "meta") val meta: Meta,
    @Json(name = "results") val results: List<AdverseEventInfo>
)

@JsonClass(generateAdapter = true)
data class Outcomes(
    @Json(name = "meta") val meta: Meta,
    @Json(name = "results") val results: List<Counts>
)

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "last_updated") val lastUpdated: String,
    @Json(name = "results") val drugCounts: DrugCounts?
)

@JsonClass(generateAdapter = true)
data class Counts(
    @Json(name = "term") val term: Int,
    @Json(name = "count") val count: Int
)

@JsonClass(generateAdapter = true)
data class AdverseEventInfo(
    @Json(name = "") val test: String
)