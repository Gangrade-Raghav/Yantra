package com.example.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.database.ExperimentResult
import com.example.data.local.database.ExperimentResultDao
import com.example.data.local.datastore.UserProfileDataStore
import com.example.data.local.static.ExperimentData
import com.example.domain.model.Experiment
import com.example.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dataStore: UserProfileDataStore,
    experimentDao: ExperimentResultDao
) : ViewModel() {

    val userProfile: StateFlow<UserProfile?> = dataStore.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val quickExperiments: StateFlow<List<Experiment>> = dataStore.userProfileFlow
        .map { profile ->
            if (profile != null) {
                ExperimentData.allExperiments.filter { profile.subjects.contains(it.subject) }.take(3)
            } else {
                emptyList()
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val recentResults: StateFlow<List<ExperimentResult>> = experimentDao.getAllResults()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
