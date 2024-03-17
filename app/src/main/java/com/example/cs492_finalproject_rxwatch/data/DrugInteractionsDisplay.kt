package com.example.cs492_finalproject_rxwatch.data

data class DrugInteractionsDisplay(
    val genericName: String,
    val manufacturerName: List<Manufacturers>
)

data class Manufacturers(
    val manufacturerName: String,
    val drugInteractions: String
)
