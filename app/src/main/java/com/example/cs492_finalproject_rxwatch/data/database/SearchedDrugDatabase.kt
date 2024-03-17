package com.example.cs492_finalproject_rxwatch.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SearchedDrug::class], version = 1)
abstract class SearchedDrugDatabase : RoomDatabase() {
    abstract fun searchedDrugDao(): SearchedDrugDAO

    companion object {
        const val DATABASE_NAME = "searched-drugs-db"

        @Volatile private var instance: SearchedDrugDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                SearchedDrugDatabase::class.java,
                DATABASE_NAME
            ).build()

        fun getInstance(context: Context): SearchedDrugDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}