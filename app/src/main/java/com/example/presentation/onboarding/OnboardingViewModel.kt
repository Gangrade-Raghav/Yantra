package com.example.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.datastore.UserProfileDataStore
import com.example.domain.model.Board
import com.example.domain.model.Subject
import com.example.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val dataStore: UserProfileDataStore
) : ViewModel() {

    // Using null for unselected states
    val selectedClass = MutableStateFlow<Int?>(null)
    val selectedBoard = MutableStateFlow<Board?>(null)
    val selectedSubjects = MutableStateFlow<Set<Subject>>(emptySet())
    val studentName = MutableStateFlow("")
    val schoolName = MutableStateFlow("")
    val rollNumber = MutableStateFlow("")

    val isUserProfileComplete: StateFlow<Boolean> = dataStore.userProfileFlow
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun toggleSubject(subject: Subject) {
        val current = selectedSubjects.value.toMutableSet()
        if (current.contains(subject)) {
            current.remove(subject)
        } else {
            current.add(subject)
        }
        selectedSubjects.value = current
    }

    fun completeOnboarding() {
        val classVal = selectedClass.value ?: return
        val boardVal = selectedBoard.value ?: return
        val nameVal = studentName.value.ifBlank { return }
        val schoolVal = schoolName.value.ifBlank { return }
        val subjectsVal = selectedSubjects.value.toList().ifEmpty { return }

        val profile = UserProfile(
            name = nameVal.trim(),
            className = classVal,
            board = boardVal,
            schoolName = schoolVal.trim(),
            subjects = subjectsVal,
            rollNumber = rollNumber.value.trim()
        )
        viewModelScope.launch {
            dataStore.saveUserProfile(profile)
        }
    }
}
