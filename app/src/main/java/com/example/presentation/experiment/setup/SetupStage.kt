package com.example.presentation.experiment.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.presentation.experiment.ExperimentViewModel

@Composable
fun SetupStage(
    viewModel: ExperimentViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val experiment = viewModel.experiment
    val setupInputs by viewModel.setupInputs.collectAsState()
    
    val allInputsValid = experiment.setupSteps.filter { it.requiresInput }.all { step ->
        setupInputs[step.inputKey]?.isNotBlank() == true
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Setup") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onNext,
                enabled = allInputsValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp)
            ) {
                Text("START EXPERIMENT")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            experiment.setupSteps.forEach { step ->
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        "${step.stepNumber}.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(step.instruction, style = MaterialTheme.typography.bodyLarge)
                }
                
                if (step.requiresInput) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = setupInputs[step.inputKey] ?: "",
                        onValueChange = { viewModel.updateSetupInput(step.inputKey, it) },
                        label = { Text(step.inputLabel) },
                        suffix = { if (step.inputUnit.isNotEmpty()) Text(step.inputUnit) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(start = 24.dp) // indent
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
