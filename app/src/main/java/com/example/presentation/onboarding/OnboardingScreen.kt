package com.example.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.Board
import com.example.domain.model.Subject

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val isComplete by viewModel.isUserProfileComplete.collectAsState()
    LaunchedEffect(isComplete) {
        if (isComplete) {
            onFinishOnboarding()
        }
    }

    var currentStep by remember { mutableIntStateOf(0) }
    var legalAgreed by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    if (currentStep < 4) {
                        currentStep++
                    } else {
                        viewModel.completeOnboarding()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                enabled = when (currentStep) {
                    0 -> legalAgreed
                    1 -> viewModel.selectedClass.collectAsState().value != null
                    2 -> viewModel.selectedBoard.collectAsState().value != null
                    3 -> viewModel.selectedSubjects.collectAsState().value.isNotEmpty()
                    4 -> {
                        val name by viewModel.studentName.collectAsState()
                        val school by viewModel.schoolName.collectAsState()
                        name.isNotBlank() && school.isNotBlank()
                    }
                    else -> false
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (currentStep == 4) "GET STARTED" else "NEXT",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Progress Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentStep > 0) {
                    Text(
                        text = "BACK",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { currentStep-- }
                    )
                } else {
                    Spacer(modifier = Modifier.width(40.dp))
                }
                
                Text(
                    text = "${currentStep + 1} / 5",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(40.dp))
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            when (currentStep) {
                0 -> LegalAgreementStep(legalAgreed) { legalAgreed = it }
                1 -> ClassSelectionStep(viewModel)
                2 -> BoardSelectionStep(viewModel)
                3 -> SubjectSelectionStep(viewModel)
                4 -> DetailsEntryStep(viewModel)
            }
        }
    }
}

@Composable
fun ColumnScope.LegalAgreementStep(isAgreed: Boolean, onAgreedChange: (Boolean) -> Unit) {
    Text(
        text = "Legal Agreements",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(16.dp))
    
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    text = LEGAL_DOCUMENTATION_TEXT,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAgreedChange(!isAgreed) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isAgreed,
            onCheckedChange = { onAgreedChange(it) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "I have read and agree to all terms and conditions, EULA, and privacy policy.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ClassSelectionStep(viewModel: OnboardingViewModel) {
    val selectedClass by viewModel.selectedClass.collectAsState()
    
    Text(
        text = "Which class are you in?",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(32.dp))
    
    val classes = listOf(9, 10, 11, 12)
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        classes.forEach { classNumber ->
            SelectableCard(
                text = "Class $classNumber",
                isSelected = selectedClass == classNumber,
                onClick = { viewModel.selectedClass.value = classNumber }
            )
        }
    }
}

@Composable
fun BoardSelectionStep(viewModel: OnboardingViewModel) {
    val selectedBoard by viewModel.selectedBoard.collectAsState()
    
    Text(
        text = "Select your educational board",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(32.dp))
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Board.entries.forEach { board ->
            SelectableCard(
                text = board.name.replace("_", " "),
                isSelected = selectedBoard == board,
                onClick = { viewModel.selectedBoard.value = board }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubjectSelectionStep(viewModel: OnboardingViewModel) {
    val selectedSubjects by viewModel.selectedSubjects.collectAsState()
    
    Text(
        text = "Choose your subjects",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Select all that apply",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(32.dp))
    
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Subject.entries.forEach { subject ->
            val isSelected = selectedSubjects.contains(subject)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        1.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(24.dp)
                    )
                    .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface)
                    .clickable { viewModel.toggleSubject(subject) }
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = subject.name.lowercase().replaceFirstChar { it.uppercase() }.replace("_", " "),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun DetailsEntryStep(viewModel: OnboardingViewModel) {
    val name by viewModel.studentName.collectAsState()
    val school by viewModel.schoolName.collectAsState()
    val rollNumber by viewModel.rollNumber.collectAsState()
    
    Text(
        text = "Almost done",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(32.dp))
    
    OutlinedTextField(
        value = name,
        onValueChange = { viewModel.studentName.value = it },
        label = { Text("Full Name") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    OutlinedTextField(
        value = school,
        onValueChange = { viewModel.schoolName.value = it },
        label = { Text("School Name") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    OutlinedTextField(
        value = rollNumber,
        onValueChange = { viewModel.rollNumber.value = it },
        label = { Text("Roll Number (Optional)") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun SelectableCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

