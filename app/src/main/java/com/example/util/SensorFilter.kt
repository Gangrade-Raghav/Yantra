package com.example.util

import kotlin.math.abs

object SensorFilter {

    fun lowPass(input: FloatArray, output: FloatArray, alpha: Float = 0.1f): FloatArray {
        for (i in input.indices) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
        return output
    }

    fun highPass(input: FloatArray, gravity: FloatArray, alpha: Float = 0.8f): FloatArray {
        val output = FloatArray(input.size)
        // Gravity update
        for (i in input.indices) {
            gravity[i] = alpha * gravity[i] + (1 - alpha) * input[i]
            output[i] = input[i] - gravity[i]
        }
        return output
    }

    fun runningAverage(newValue: Float, history: ArrayDeque<Float>, windowSize: Int = 10): Float {
        history.addLast(newValue)
        if (history.size > windowSize) {
            history.removeFirst()
        }
        return history.sum() / history.size
    }

    fun detectPeaks(data: List<Float>, threshold: Float, minDistance: Int): List<Int> {
        val peaks = mutableListOf<Int>()
        var i = 1
        var lastPeakIndex = -minDistance
        while (i < data.size - 1) {
            val prev = data[i - 1]
            val curr = data[i]
            val next = data[i + 1]

            if (curr > threshold && curr > prev && curr > next) {
                if (i - lastPeakIndex >= minDistance) {
                    peaks.add(i)
                    lastPeakIndex = i
                }
            }
            i++
        }
        return peaks
    }

    fun detectZeroCrossings(samples: ShortArray, sampleRate: Int): Double {
        var crossings = 0
        for (i in 1 until samples.size) {
            if ((samples[i - 1] > 0 && samples[i] <= 0) || (samples[i - 1] < 0 && samples[i] >= 0)) {
                crossings++
            }
        }
        val durationSeconds = samples.size.toDouble() / sampleRate
        val totalCycles = crossings / 2.0
        return if (durationSeconds > 0) totalCycles / durationSeconds else 0.0
    }
}
