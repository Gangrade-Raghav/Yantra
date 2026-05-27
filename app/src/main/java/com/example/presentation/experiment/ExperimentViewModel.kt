package com.example.presentation.experiment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.database.ExperimentResult
import com.example.data.local.database.ExperimentResultDao
import com.example.data.local.datastore.UserProfileDataStore
import com.example.data.local.static.ExperimentData
import com.example.domain.model.Experiment
import com.example.domain.model.LiveExperimentState
import com.example.domain.model.SensorReading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ExperimentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val experimentResultDao: ExperimentResultDao,
    private val userProfileDataStore: UserProfileDataStore
) : ViewModel() {

    val experimentId: String = savedStateHandle.get<String>("experimentId") ?: ""
    val experiment: Experiment = ExperimentData.allExperiments.first { it.id == experimentId }

    val userProfile = userProfileDataStore.userProfileFlow.stateIn(
        viewModelScope, SharingStarted.Eagerly, null
    )

    private val _currentStage = MutableStateFlow(ExperimentStage.BRIEFING)
    val currentStage = _currentStage.asStateFlow()

    // Setup measurements (Key -> Value)
    private val _setupInputs = MutableStateFlow<Map<String, String>>(emptyMap())
    val setupInputs = _setupInputs.asStateFlow()

    // Live Collection State
    private val _liveState = MutableStateFlow<LiveExperimentState?>(null)
    val liveState = _liveState.asStateFlow()

    // Analysis Results
    private val _calculatedResults = MutableStateFlow<Map<String, Double>>(emptyMap())
    val calculatedResults = _calculatedResults.asStateFlow()
    
    private val _percentageError = MutableStateFlow<Double?>(null)
    val percentageError = _percentageError.asStateFlow()

    fun updateSetupInput(key: String, value: String) {
        _setupInputs.value = _setupInputs.value.toMutableMap().apply { put(key, value) }
    }

    fun nextStage() {
        val next = when (_currentStage.value) {
            ExperimentStage.BRIEFING -> ExperimentStage.DEVICE_CHECK
            ExperimentStage.DEVICE_CHECK -> ExperimentStage.SETUP
            ExperimentStage.SETUP -> {
                initLiveState()
                ExperimentStage.LIVE
            }
            ExperimentStage.LIVE -> ExperimentStage.ANALYSIS
            ExperimentStage.ANALYSIS -> ExperimentStage.REPORT
            ExperimentStage.REPORT -> ExperimentStage.REPORT // end
        }
        _currentStage.value = next
    }

    fun previousStage() {
        val prev = when (_currentStage.value) {
            ExperimentStage.BRIEFING -> ExperimentStage.BRIEFING
            ExperimentStage.DEVICE_CHECK -> ExperimentStage.BRIEFING
            ExperimentStage.SETUP -> ExperimentStage.DEVICE_CHECK
            ExperimentStage.LIVE -> ExperimentStage.SETUP
            ExperimentStage.ANALYSIS -> ExperimentStage.LIVE
            ExperimentStage.REPORT -> ExperimentStage.ANALYSIS
        }
        _currentStage.value = prev
    }

    private fun initLiveState() {
        val firstInstruction = experiment.liveInstructions.firstOrNull()
        if (firstInstruction != null) {
            _liveState.value = LiveExperimentState(
                currentInstruction = firstInstruction,
                instructionIndex = 0,
                trialNumber = 1,
                totalTrials = 3, // Defaults depending on experiment
                capturedReadings = emptyList(),
                isCollecting = false,
                isPaused = false,
                errorMessage = null,
                isComplete = false
            )
        }
    }
    
    fun updateLiveState(newState: LiveExperimentState) {
        _liveState.value = newState
    }
    
    fun setCalculatedResults(results: Map<String, Double>, pctError: Double?) {
        _calculatedResults.value = results
        _percentageError.value = pctError
    }

    fun markExperimentComplete() {
        viewModelScope.launch {
            val calcResultsStr = _calculatedResults.value.entries.joinToString { "${it.key}: ${it.value}" }
            
            val result = ExperimentResult(
                id = UUID.randomUUID().toString(),
                experimentId = experimentId,
                performedDate = System.currentTimeMillis(),
                rawSensorData = "{}",
                setupMeasurements = _setupInputs.value.entries.joinToString { "${it.key}: ${it.value}" },
                calculatedResults = calcResultsStr,
                percentageError = _percentageError.value ?: 0.0,
                reportPdfPath = null,
                durationSeconds = 60, // Dummy
                trialCount = 1
            )
            experimentResultDao.insertResult(result)
        }
    }

    // Helper functions for actual live logic will be placed in specific experiment runners
}

enum class ExperimentStage {
    BRIEFING, DEVICE_CHECK, SETUP, LIVE, ANALYSIS, REPORT
}
