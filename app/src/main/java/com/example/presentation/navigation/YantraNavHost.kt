package com.example.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.onboarding.OnboardingScreen
import com.example.presentation.home.HomeScreen
import com.example.presentation.library.LibraryScreen
import com.example.presentation.progress.ProgressScreen
import com.example.presentation.experiment.ExperimentFlowScreen
import com.example.presentation.legal.LegalScreen

@Composable
fun YantraNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "onboarding"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToLibrary = { navController.navigate("library") },
                onNavigateToProgress = { navController.navigate("progress") },
                onNavigateToExperiment = { id -> navController.navigate("experiment/$id") },
                onNavigateToLegal = { navController.navigate("legal") }
            )
        }
        composable("library") {
            LibraryScreen(
                onNavigateHome = { navController.popBackStack() },
                onNavigateToExperiment = { id -> navController.navigate("experiment/$id") }
            )
        }
        composable("progress") {
            ProgressScreen(
                onNavigateHome = { navController.popBackStack() }
            )
        }
        composable("experiment/{experimentId}") { backStackEntry ->
            // ExperimentViewModel expects this
            ExperimentFlowScreen(
                onExit = { navController.popBackStack() }
            )
        }
        composable("legal") {
            LegalScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
