package com.example.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperimentResultDao {
    @Query("SELECT * FROM experiment_results ORDER BY performedDate DESC")
    fun getAllResults(): Flow<List<ExperimentResult>>

    @Query("SELECT * FROM experiment_results WHERE experimentId = :experimentId ORDER BY performedDate DESC")
    fun getResultsForExperiment(experimentId: String): Flow<List<ExperimentResult>>

    @Query("SELECT * FROM experiment_results WHERE id = :id LIMIT 1")
    suspend fun getResultById(id: String): ExperimentResult?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ExperimentResult)
}
