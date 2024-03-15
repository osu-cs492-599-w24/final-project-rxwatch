package com.example.cs492_finalproject_rxwatch.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DrugInformation (
    val meta: DrugLabelMeta,
    val results: List<DrugWarnings>
)

@JsonClass(generateAdapter = true)
data class DrugLabelMeta (
    @Json(name = "last_updated") val lastUpdated: String,
    val results: LabelCounts
)

@JsonClass(generateAdapter = true)
data class LabelCounts (
    val total: Int
)

@JsonClass(generateAdapter = true)
data class DrugWarnings (
    val warnings: List<String>,
    @Json(name = "do_not_use") val doNotUse: List<String>
)