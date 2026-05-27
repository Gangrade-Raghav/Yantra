package com.example.util

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class SensorFilterTest {

    @Test
    fun testLowPass() {
        val input = floatArrayOf(10f, 10f, 10f)
        val output = floatArrayOf(0f, 0f, 0f)
        val result = SensorFilter.lowPass(input, output, 0.1f)
        
        assertArrayEquals(floatArrayOf(1f, 1f, 1f), result, 0.001f)
    }

    @Test
    fun testHighPass() {
        val input = floatArrayOf(10f, 10f, 10f)
        val gravity = floatArrayOf(8f, 8f, 8f)
        val result = SensorFilter.highPass(input, gravity, 0.8f)
        // newGravity = 0.8*8 + 0.2*10 = 6.4 + 2.0 = 8.4
        // output = input - newGravity = 10 - 8.4 = 1.6
        assertArrayEquals(floatArrayOf(1.6f, 1.6f, 1.6f), result, 0.001f)
    }

    @Test
    fun testRunningAverage() {
        val history = ArrayDeque<Float>()
        SensorFilter.runningAverage(10f, history, 3)
        SensorFilter.runningAverage(20f, history, 3)
        val result = SensorFilter.runningAverage(30f, history, 3)
        assertEquals(20f, result, 0.001f)
    }

    @Test
    fun testDetectPeaks() {
        val data = listOf(1f, 2f, 5f, 2f, 1f, 1f, 6f, 1f)
        val peaks = SensorFilter.detectPeaks(data, threshold = 3f, minDistance = 2)
        assertEquals(listOf(2, 6), peaks)
    }

    @Test
    fun testZeroCrossings() {
        val samples = shortArrayOf(100, -100, 100, -100) // 3 crossings = 1.5 cycles
        val sampleRate = 4
        // duration = 4/4 = 1.0s
        // freq = 1.5/1.0 = 1.5Hz
        val freq = SensorFilter.detectZeroCrossings(samples, sampleRate)
        assertEquals(1.5, freq, 0.01)
    }
}
