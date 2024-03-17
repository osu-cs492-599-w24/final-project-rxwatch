package com.example.cs492_finalproject_rxwatch.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchedDrugDAO {
    @Insert
    suspend fun insert(drug: SearchedDrug)

    @Delete
    suspend fun delete(drug: SearchedDrug)

    @Query("DELETE FROM SearchedDrug WHERE drugName = :name")
    suspend fun deleteDrugByName(name: String)

    @Query("SELECT * FROM SearchedDrug")
    fun getAllSearchedDrugs(): Flow<List<SearchedDrug>>
}