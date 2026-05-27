package com.example.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "experiment_results")
data class ExperimentResult(
    @PrimaryKey val id: String,
    val experimentId: String,
    val performedDate: Long,
    val rawSensorData: String,
    val setupMeasurements: String,
    val calculatedResults: String,
    val percentageError: Double?,
    val reportPdfPath: String?,
    val durationSeconds: Int,
    val trialCount: Int
)
