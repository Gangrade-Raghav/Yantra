package com.example.sensor

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log10
import com.example.util.SensorFilter

data class AudioFrame(
    val rawSamples: ShortArray,
    val dominantFrequency: Double,
    val decibelLevel: Double,
    val timestamp: Long
)

class MicrophoneSensor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getAudioFlow(): Flow<AudioFrame>? {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return null
        }

        val sampleRate = 44100
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) return null

        val audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        return callbackFlow {
            if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
                close()
                return@callbackFlow
            }

            audioRecord.startRecording()

            launch(Dispatchers.IO) {
                val buffer = ShortArray(bufferSize)
                while (isActive) {
                    val readResult = audioRecord.read(buffer, 0, buffer.size)
                    if (readResult > 0) {
                        val frameData = buffer.copyOf(readResult)
                        
                        // Calculate dB
                        var sumSquared = 0.0
                        for (sample in frameData) {
                            val norm = sample / 32768.0
                            sumSquared += norm * norm
                        }
                        val rms = Math.sqrt(sumSquared / readResult)
                        val decibel = if (rms > 0) 20 * log10(rms) else -100.0

                        // Calculate freq approx
                        val freq = SensorFilter.detectZeroCrossings(frameData, sampleRate)

                        trySend(
                            AudioFrame(
                                rawSamples = frameData,
                                dominantFrequency = freq,
                                decibelLevel = decibel,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }

            awaitClose {
                audioRecord.stop()
                audioRecord.release()
            }
        }
    }
}
