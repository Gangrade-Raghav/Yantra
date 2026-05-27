package com.example.presentation.experiment.live.runners

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.presentation.experiment.ExperimentViewModel
import kotlinx.coroutines.delay

@Composable
fun ReactionTimeRunner(
    parentViewModel: ExperimentViewModel,
    onNext: () -> Unit
) {
    var state by remember { mutableStateOf(ReactionState.WAITING_TO_START) }
    var startTime by remember { mutableLongStateOf(0L) }
    var reactionTime by remember { mutableLongStateOf(0L) }
    var trial by remember { mutableIntStateOf(1) }
    val times = remember { mutableStateListOf<Long>() }
    var resultText by remember { mutableStateOf("Tap to start") }

    val color = when (state) {
        ReactionState.WAITING_TO_START -> MaterialTheme.colorScheme.surface
        ReactionState.WAIT_FOR_GREEN -> Color.Red
        ReactionState.TAP_NOW -> Color.Green
        ReactionState.EARLY -> Color.Red
        ReactionState.RESULT -> MaterialTheme.colorScheme.primaryContainer
    }

    LaunchedEffect(state) {
        if (state == ReactionState.WAIT_FOR_GREEN) {
            val randomDelay = (2000L..5000L).random()
            delay(randomDelay)
            if (state == ReactionState.WAIT_FOR_GREEN) {
                state = ReactionState.TAP_NOW
                startTime = System.currentTimeMillis()
                resultText = "TAP!"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .clickable {
                when (state) {
                    ReactionState.WAITING_TO_START -> {
                        state = ReactionState.WAIT_FOR_GREEN
                        resultText = "Wait for Green..."
                    }
                    ReactionState.WAIT_FOR_GREEN -> {
                        state = ReactionState.EARLY
                        resultText = "Too early! Tap to try again."
                    }
                    ReactionState.TAP_NOW -> {
                        reactionTime = System.currentTimeMillis() - startTime
                        times.add(reactionTime)
                        if (trial < 5) {
                            state = ReactionState.RESULT
                            resultText = "Time: ${reactionTime}ms\nTap for Trial ${trial + 1}"
                            trial++
                        } else {
                            // Finish
                            val avg = times.average()
                            val pctError = Math.abs(avg - 250.0) / 250.0 * 100
                            parentViewModel.setCalculatedResults(
                                mapOf("Average Time" to avg),
                                pctError
                            )
                            onNext()
                        }
                    }
                    ReactionState.EARLY -> {
                        state = ReactionState.WAIT_FOR_GREEN
                        resultText = "Wait for Green..."
                    }
                    ReactionState.RESULT -> {
                        state = ReactionState.WAIT_FOR_GREEN
                        resultText = "Wait for Green..."
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(resultText, style = MaterialTheme.typography.headlineLarge, color = if (state == ReactionState.WAIT_FOR_GREEN || state == ReactionState.TAP_NOW || state == ReactionState.EARLY) Color.White else MaterialTheme.colorScheme.onSurface)
    }
}

enum class ReactionState {
    WAITING_TO_START, WAIT_FOR_GREEN, TAP_NOW, EARLY, RESULT
}
