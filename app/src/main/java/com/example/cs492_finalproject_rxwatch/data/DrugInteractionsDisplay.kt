package com.example.cs492_finalproject_rxwatch.data

import java.io.Serializable

data class DrugInteractionsDisplay(
    val genericName: String,
    val manufacturers: List<Manufacturer>
) : Serializable

data class Manufacturer(
    val manufacturerName: String,
    val drugInteractions: String
) : Serializable