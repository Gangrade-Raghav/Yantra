package com.example.sensor

import com.example.domain.model.SensorReading
import com.example.domain.model.SensorType
import com.example.util.SensorFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.sqrt

class AccelerometerSensor @Inject constructor(
    private val sensorManagerWrapper: SensorManagerWrapper
) {
    private val gravity = floatArrayOf(0f, 0f, 0f)

    fun getLinearAccelerationFlow(): Flow<FloatArray>? {
        return sensorManagerWrapper.getSensorFlow(SensorType.ACCELEROMETER)?.map { reading ->
            SensorFilter.highPass(reading.values, gravity, 0.8f)
        }
    }

    fun getMagnitudeFlow(): Flow<Float>? {
        return getLinearAccelerationFlow()?.map { linearAccel ->
            sqrt(linearAccel[0] * linearAccel[0] + linearAccel[1] * linearAccel[1] + linearAccel[2] * linearAccel[2])
        }
    }
}
