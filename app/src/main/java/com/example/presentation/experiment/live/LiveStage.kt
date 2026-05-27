package com.example.presentation.experiment.live

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.experiment.ExperimentViewModel
import com.example.presentation.experiment.live.runners.PendulumRunner
import com.example.presentation.experiment.live.runners.ReactionTimeRunner
import com.example.presentation.experiment.live.runners.GenericRunner

@Composable
fun LiveStage(
    viewModel: ExperimentViewModel,
    onNext: () -> Unit
) {
    val experiment = viewModel.experiment
    
    // Dispatch to the correct runner based on experiment ID
    when (experiment.id) {
        "physics_pendulum_g" -> PendulumRunner(viewModel, onNext)
        "bio_reaction_time" -> ReactionTimeRunner(viewModel, onNext)
        else -> GenericRunner(viewModel, onNext)
    }
}
