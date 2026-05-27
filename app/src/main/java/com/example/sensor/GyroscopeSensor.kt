package com.example.sensor

import com.example.domain.model.SensorType
import javax.inject.Inject

class GyroscopeSensor @Inject constructor(
    private val sensorManagerWrapper: SensorManagerWrapper
) {
    fun getAngularVelocityFlow() = sensorManagerWrapper.getSensorFlow(SensorType.GYROSCOPE)
}
