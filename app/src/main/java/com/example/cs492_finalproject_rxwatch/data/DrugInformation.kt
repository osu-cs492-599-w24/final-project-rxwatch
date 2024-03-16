package com.example.cs492_finalproject_rxwatch.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
* The DrugInformation data class is the primary data class used to
* retrieve the drug name and interactions from the API label endpoint.
*
* Example query:
* https://api.fda.gov/drug/label.json?search=(drug_interactions:($searchedDrugName))+(_exists_:openfda.generic_name)
* */
@JsonClass(generateAdapter = true)
data class DrugInformation (
    val meta: DrugLabelMeta,
    val results: List<DrugInfo>
)

/*
* DrugLabelMeta is a simple data class to help gather the total number of results in the database.
* */
@JsonClass(generateAdapter = true)
data class DrugLabelMeta (
    @Json(name = "last_updated") val lastUpdated: String,
    @Json(name = "results") val drugCounts: DrugCounts
)

/*
* LabelCounts gets the total number of occurrences in the FDA database that match
* the query parameters without the limit,
* and the limit (which should match the length of results).
* */
@JsonClass(generateAdapter = true)
data class DrugCounts (
    val limit: Int,
    val total: Int
)

/*
* DrugInfo data class gets the two drug interactions fields from the API response:
* drug_interactions: A long string
* drug_interactions_table: A long string with HTML tags and syntax
* Also gets the name of the drug via the openfda field
* */
@JsonClass(generateAdapter = true)
data class DrugInfo (
    @Json(name = "drug_interactions") val drugInteractionsString: List<String>?,
    @Json(name = "drug_interactions_table") val drugInteractionsTable: List<String>?,
    @Json(name = "openfda") val openFDA: OpenFDA?
)

/*
* OpenFDA data class helps get the drug name fields.
* The generic_name field is the primary name of interest.
* */
@JsonClass(generateAdapter = true)
data class OpenFDA (
    @Json(name = "generic_name") val genericName : List<String>?,
    @Json(name = "brand_name") val brandName: List<String>?,
    @Json(name = "manufacturer_name") val manufacturerName: List<String>?
)