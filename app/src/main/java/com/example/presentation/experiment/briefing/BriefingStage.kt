package com.example.presentation.experiment.briefing

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.experiment.ExperimentViewModel

@Composable
fun BriefingStage(
    viewModel: ExperimentViewModel,
    onExit: () -> Unit,
    onNext: () -> Unit
) {
    val experiment = viewModel.experiment

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text(experiment.name) },
                navigationIcon = {
                    IconButton(onClick = onExit) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp)
            ) {
                Text("PROCEED")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text("Aim", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(experiment.aim, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Principle", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(experiment.principle, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))

            Text("What Yantra Measures", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(experiment.whatPhoneMeasures, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
