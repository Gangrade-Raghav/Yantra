package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.domain.model.SensorReading
import com.example.domain.model.SensorType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorManagerWrapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    fun getSensorFlow(sensorType: SensorType): Flow<SensorReading>? {
        val androidSensorType = when (sensorType) {
            SensorType.ACCELEROMETER -> Sensor.TYPE_ACCELEROMETER
            SensorType.GYROSCOPE -> Sensor.TYPE_GYROSCOPE
            SensorType.MAGNETOMETER -> Sensor.TYPE_MAGNETIC_FIELD
            SensorType.LIGHT -> Sensor.TYPE_LIGHT
            SensorType.BAROMETER -> Sensor.TYPE_PRESSURE
            SensorType.PROXIMITY -> Sensor.TYPE_PROXIMITY
            SensorType.ROTATION_VECTOR -> Sensor.TYPE_ROTATION_VECTOR
            // Handled separately:
            SensorType.MICROPHONE, SensorType.CAMERA, SensorType.GPS -> return null
        }

        val sensor = sensorManager.getDefaultSensor(androidSensorType) ?: return null

        val delay = when (sensorType) {
            SensorType.ACCELEROMETER -> SensorManager.SENSOR_DELAY_FASTEST
            SensorType.GYROSCOPE -> SensorManager.SENSOR_DELAY_GAME
            else -> SensorManager.SENSOR_DELAY_NORMAL
        }

        return callbackFlow {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val reading = SensorReading(
                        timestamp = event.timestamp,
                        values = event.values.clone(),
                        sensorType = sensorType,
                        accuracy = event.accuracy
                    )
                    trySend(reading)
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Accuracy changes can be emitted or logged if needed
                }
            }

            sensorManager.registerListener(listener, sensor, delay)

            awaitClose {
                sensorManager.unregisterListener(listener)
            }
        }
    }
}
