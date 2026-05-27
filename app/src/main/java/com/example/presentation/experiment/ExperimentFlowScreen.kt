package com.example.presentation.experiment

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.experiment.briefing.BriefingStage
import com.example.presentation.experiment.devicecheck.DeviceCheckStage
import com.example.presentation.experiment.setup.SetupStage
import com.example.presentation.experiment.live.LiveStage
import com.example.presentation.experiment.analysis.AnalysisStage
import com.example.presentation.experiment.report.ReportStage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExperimentFlowScreen(
    onExit: () -> Unit,
    viewModel: ExperimentViewModel = hiltViewModel()
) {
    val currentStage by viewModel.currentStage.collectAsState()

    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Abandon Experiment?") },
            text = { Text("Your progress so far will be lost.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onExit()
                }) { Text("Abandon") }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) { Text("Cancel") }
            }
        )
    }

    AnimatedContent(
        targetState = currentStage,
        transitionSpec = {
            if (targetState.ordinal > initialState.ordinal) {
                slideInHorizontally(animationSpec = tween(300)) { it } + fadeIn() togetherWith 
                slideOutHorizontally(animationSpec = tween(300)) { -it } + fadeOut()
            } else {
                slideInHorizontally(animationSpec = tween(300)) { -it } + fadeIn() togetherWith 
                slideOutHorizontally(animationSpec = tween(300)) { it } + fadeOut()
            }
        }, label = "stage_transition"
    ) { stage ->
        when (stage) {
            ExperimentStage.BRIEFING -> BriefingStage(viewModel, onExit = { showExitDialog = true }, onNext = { viewModel.nextStage() })
            ExperimentStage.DEVICE_CHECK -> DeviceCheckStage(viewModel, onBack = { viewModel.previousStage() }, onNext = { viewModel.nextStage() })
            ExperimentStage.SETUP -> SetupStage(viewModel, onBack = { viewModel.previousStage() }, onNext = { viewModel.nextStage() })
            ExperimentStage.LIVE -> LiveStage(viewModel, onNext = { viewModel.nextStage() }) // Live stage shouldn't have simple back, handled internally
            ExperimentStage.ANALYSIS -> AnalysisStage(viewModel, onNext = { viewModel.nextStage() })
            ExperimentStage.REPORT -> ReportStage(viewModel, onFinish = onExit)
        }
    }
}
