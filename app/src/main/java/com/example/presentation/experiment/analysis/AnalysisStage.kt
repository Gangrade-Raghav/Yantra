package com.example.presentation.experiment.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.experiment.ExperimentViewModel

@Composable
fun AnalysisStage(
    viewModel: ExperimentViewModel,
    onNext: () -> Unit
) {
    val experiment = viewModel.experiment
    val calculatedResults by viewModel.calculatedResults.collectAsState()
    val pctError by viewModel.percentageError.collectAsState()

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    viewModel.markExperimentComplete()
                    onNext()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp)
            ) {
                Text("GENERATE REPORT")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text("Analysis", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Calculated Results:", style = MaterialTheme.typography.titleMedium)
            calculatedResults.forEach { (key, value) ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(key, style = MaterialTheme.typography.bodyLarge)
                    Text(String.format("%.2f", value), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                }
            }

            if (pctError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Percentage Error:", style = MaterialTheme.typography.bodyLarge)
                    Text(String.format("%.2f%%", pctError), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
