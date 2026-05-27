package com.example.sensor

import com.example.domain.model.SensorType
import javax.inject.Inject

class LightSensor @Inject constructor(
    private val sensorManagerWrapper: SensorManagerWrapper
) {
    fun getLightFlow() = sensorManagerWrapper.getSensorFlow(SensorType.LIGHT)
}
