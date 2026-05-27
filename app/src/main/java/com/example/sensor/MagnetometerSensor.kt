package com.example.sensor

import com.example.domain.model.SensorType
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlin.math.sqrt
import com.example.util.SensorFilter

class MagnetometerSensor @Inject constructor(
    private val sensorManagerWrapper: SensorManagerWrapper
) {
    private val historyX = ArrayDeque<Float>()
    private val historyY = ArrayDeque<Float>()
    private val historyZ = ArrayDeque<Float>()

    fun getSmoothedMagnitudeFlow() = sensorManagerWrapper.getSensorFlow(SensorType.MAGNETOMETER)?.map { reading ->
        val x = SensorFilter.runningAverage(reading.values[0], historyX)
        val y = SensorFilter.runningAverage(reading.values[1], historyY)
        val z = SensorFilter.runningAverage(reading.values[2], historyZ)
        
        sqrt(x * x + y * y + z * z)
    }
}
