package com.example.cs492_finalproject_rxwatch.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class SearchedDrug(
    @PrimaryKey val drugName: String,
    val timestamp: Long
) : Serializable