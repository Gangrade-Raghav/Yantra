package com.example.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.database.ExperimentResultDao
import com.example.data.local.static.ExperimentData
import com.example.domain.model.Experiment
import com.example.domain.model.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    experimentResultDao: ExperimentResultDao
) : ViewModel() {

    val selectedSubject = MutableStateFlow<Subject?>(null)

    val completedExperimentIds = experimentResultDao.getAllResults()
        .map { results -> results.map { it.experimentId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())

    val groupedExperiments = combine(selectedSubject) { (subject) ->
        val filtered = if (subject == null) {
            ExperimentData.allExperiments
        } else {
            ExperimentData.allExperiments.filter { it.subject == subject }
        }
        filtered.groupBy { it.chapterName }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ExperimentData.allExperiments.groupBy { it.chapterName })
}
