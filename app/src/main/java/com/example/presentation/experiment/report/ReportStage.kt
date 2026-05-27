package com.example.presentation.experiment.report

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.presentation.experiment.ExperimentViewModel
import com.example.pdf.ReportGenerator
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ReportStage(
    viewModel: ExperimentViewModel,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val reportGenerator = remember { ReportGenerator() }

    val userProfile by viewModel.userProfile.collectAsState()
    val results by viewModel.calculatedResults.collectAsState()
    val error by viewModel.percentageError.collectAsState()
    
    var generatedFile by remember { mutableStateOf<File?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var generationString by remember { mutableStateOf("Generating Report...") }

    Scaffold(
        bottomBar = {
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp)
            ) {
                Text("FINISH EXPERIMENT")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (generatedFile == null && !isGenerating) {
                Text(
                    "Ready to generate report for ${viewModel.experiment.name}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {
                    isGenerating = true
                    generationString = "Generating Report..."
                    coroutineScope.launch {
                        try {
                            userProfile?.let {
                                generatedFile = reportGenerator.generateReport(
                                    context,
                                    it,
                                    viewModel.experiment,
                                    results,
                                    error
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            generationString = "Error: " + e.message
                        }
                        isGenerating = false
                    }
                }) {
                    Text("GENERATE PDF REPORT")
                }
            } else if (isGenerating) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(generationString)
            } else if (generatedFile != null) {
                Text(
                    "Report Generated Successfully!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "File ready to share.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        val file = generatedFile
                        if (file != null) {
                            try {
                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "application/pdf"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    putExtra(
                                        Intent.EXTRA_TEXT, 
                                        "${userProfile?.name} has completed ${viewModel.experiment.name}. Lab report attached. Generated using Yantra."
                                    )
                                    setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share Report Via"))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("SHARE VIA WHATSAPP / EMAIL")
                }
            } else {
                 Text(generationString, color = MaterialTheme.colorScheme.error)
                 Spacer(modifier = Modifier.height(16.dp))
                 Button(onClick = { isGenerating = false }) { Text("Retry") }
            }
        }
    }
}

