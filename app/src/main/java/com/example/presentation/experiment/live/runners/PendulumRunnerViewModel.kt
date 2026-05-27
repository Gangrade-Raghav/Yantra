package com.example.presentation.experiment.live.runners

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.LiveExperimentState
import com.example.domain.model.SensorReading
import com.example.sensor.GyroscopeSensor
import com.example.util.SensorFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendulumRunnerViewModel @Inject constructor(
    private val gyroscopeSensor: GyroscopeSensor
) : ViewModel() {

    private val _angularVelocityZ = MutableStateFlow<Float>(0f)
    val angularVelocityZ = _angularVelocityZ.asStateFlow()

    private val _oscillationsCount = MutableStateFlow(0)
    val oscillationsCount = _oscillationsCount.asStateFlow()

    private val _isCollecting = MutableStateFlow(false)
    val isCollecting = _isCollecting.asStateFlow()

    private var sensorJob: Job? = null
    private val zData = mutableListOf<Float>()
    private var startTime = 0L

    fun startCollection() {
        if (_isCollecting.value) return
        _isCollecting.value = true
        _oscillationsCount.value = 0
        zData.clear()
        
        sensorJob = viewModelScope.launch {
            gyroscopeSensor.getAngularVelocityFlow()?.collect { reading ->
                val z = reading.values[2]
                _angularVelocityZ.value = z
                zData.add(z)
                
                if (zData.size == 1) {
                    startTime = reading.timestamp
                }

                // Periodically check for peaks
                if (zData.size % 20 == 0) {
                    // Need to find peaks
                    val peaks = SensorFilter.detectPeaks(zData.toList(), threshold = 1.0f, minDistance = 10)
                    // 2 peaks = 1 full oscillation (roughly, if we only look at positive peaks)
                    _oscillationsCount.value = peaks.size
                    
                    if (peaks.size >= 10) {
                        stopCollection()
                    }
                }
            }
        }
    }

    fun stopCollection(): Long {
        _isCollecting.value = false
        sensorJob?.cancel()
        sensorJob = null
        
        // Return duration in milliseconds
        // (Since we stop exactly at 10 peaks, the duration from start to now is the time for 10 oscillations)
        return (System.currentTimeMillis() - startTime) // Approx. Should use precise timestamps but this is a placeholder. 
        // We will refine it.
    }
}
