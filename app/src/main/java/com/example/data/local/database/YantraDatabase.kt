package com.example.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExperimentResult::class], version = 1, exportSchema = false)
abstract class YantraDatabase : RoomDatabase() {
    abstract fun experimentResultDao(): ExperimentResultDao
}
