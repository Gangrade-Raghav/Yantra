package com.example.presentation.experiment.live.runners

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.experiment.ExperimentViewModel
import kotlin.math.PI
import kotlin.math.abs

@Composable
fun PendulumRunner(
    parentViewModel: ExperimentViewModel,
    onNext: () -> Unit,
    runnerViewModel: PendulumRunnerViewModel = hiltViewModel()
) {
    val zVelocity by runnerViewModel.angularVelocityZ.collectAsState()
    val oscillations by runnerViewModel.oscillationsCount.collectAsState()
    val isCollecting by runnerViewModel.isCollecting.collectAsState()

    var trialNumber by remember { mutableIntStateOf(1) }
    var trialTimes = remember { mutableStateListOf<Long>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Trial $trialNumber of 3", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Z Angular Velocity: ${String.format("%.2f", zVelocity)}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Oscillations Detected: $oscillations / 10", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        
        Spacer(modifier = Modifier.height(32.dp))

        if (!isCollecting) {
            Button(
                onClick = {
                    runnerViewModel.startCollection()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("START TRIAL")
            }
        } else {
            Button(
                onClick = {
                    val time = runnerViewModel.stopCollection()
                    // If user stops manually before 10, just record whatever time
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("STOP", color = MaterialTheme.colorScheme.onError)
            }
        }

        // Auto transition logic
        LaunchedEffect(oscillations) {
            if (oscillations >= 10 && isCollecting) {
                val time = runnerViewModel.stopCollection()
                trialTimes.add(time)
                if (trialNumber < 3) {
                    trialNumber++
                } else {
                    // Calculate final result
                    val avgTimeFor10 = trialTimes.average()
                    val period = (avgTimeFor10 / 10.0) / 1000.0 // seconds
                    
                    val lengthCmStr = parentViewModel.setupInputs.value["string_length_cm"] ?: "100"
                    val lengthM = (lengthCmStr.toDoubleOrNull() ?: 100.0) / 100.0
                    
                    val g = (4.0 * PI * PI * lengthM) / (period * period)
                    
                    val pctError = abs(g - 9.8) / 9.8 * 100
                    
                    val map = mapOf(
                        "Average Time (10 osc)" to avgTimeFor10 / 1000.0,
                        "Calculated Period T" to period,
                        "Calculated g" to g
                    )
                    
                    parentViewModel.setCalculatedResults(map, pctError)
                    onNext()
                }
            }
        }
    }
}
