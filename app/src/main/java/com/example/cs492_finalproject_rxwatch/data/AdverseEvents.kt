package com.example.cs492_finalproject_rxwatch.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
 * Represents top-level information that is returned by API
 * which is split into meta info and results
 */
@JsonClass(generateAdapter = true)
data class Outcomes(
    @Json(name = "meta") val meta: Meta,
    @Json(name = "results") val results: List<Counts>
)

/*
 * This represents the fields in the meta section
 */
@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "last_updated") val lastUpdated: String,
    @Json(name = "results") val drugCounts: DrugCounts?
)

/*
 * This represents the individual counts for each outcome
 * that you would see in the results section.
 */
@JsonClass(generateAdapter = true)
data class Counts(
    @Json(name = "term") val term: Int,
    @Json(name = "count") val count: Int
)