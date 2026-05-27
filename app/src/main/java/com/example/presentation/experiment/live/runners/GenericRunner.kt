package com.example.presentation.experiment.live.runners

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.experiment.ExperimentViewModel
import kotlinx.coroutines.delay

@Composable
fun GenericRunner(
    parentViewModel: ExperimentViewModel,
    onNext: () -> Unit
) {
    var isRunning by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (progress < 1f) {
                delay(50)
                progress += 0.02f
            }
            // Done
            val stdVal = parentViewModel.experiment.standardValue ?: 10.0
            val randomVal = stdVal * (0.9 + Math.random() * 0.2)
            val pctError = Math.abs(randomVal - stdVal) / stdVal * 100
            
            val resultMap = mutableMapOf<String, Double>()
            resultMap["Simulated Random Value"] = randomVal
            
            parentViewModel.setCalculatedResults(resultMap, pctError)
            onNext()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Running: ${parentViewModel.experiment.name}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(32.dp))
        
        CircularProgressIndicator(progress = { progress }, modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(16.dp))
        
        if (isRunning) {
            Text("Collecting Data...", color = MaterialTheme.colorScheme.primary)
        } else {
            Button(onClick = { isRunning = true }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Text("START DATA COLLECTION")
            }
        }
    }
}
