package com.example.cs492_finalproject_rxwatch.data.database

class SearchedDrugRepository(
    private val dao: SearchedDrugDAO
) {
    suspend fun insertSearchedDrug(drug: SearchedDrug) = dao.insert(drug)
    suspend fun deleteSearchedDrug(drug: SearchedDrug) = dao.delete(drug)

    suspend fun deleteSearchedDrugByName(name: String) = dao.deleteDrugByName(name)

    fun getAllSearchedDrugs() = dao.getAllSearchedDrugs()
}