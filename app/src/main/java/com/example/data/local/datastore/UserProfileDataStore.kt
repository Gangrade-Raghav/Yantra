package com.example.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.Board
import com.example.domain.model.Subject
import com.example.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "yantra_user_prefs")

class UserProfileDataStore(private val context: Context) {

    private val NAME = stringPreferencesKey("name")
    private val CLASS_NAME = intPreferencesKey("class_name")
    private val BOARD = stringPreferencesKey("board")
    private val SCHOOL_NAME = stringPreferencesKey("school_name")
    private val SUBJECTS = stringPreferencesKey("subjects")
    private val ROLL_NUMBER = stringPreferencesKey("roll_number")

    val userProfileFlow: Flow<UserProfile?> = context.dataStore.data.map { prefs ->
        val name = prefs[NAME] ?: return@map null
        val className = prefs[CLASS_NAME] ?: return@map null
        val boardStr = prefs[BOARD] ?: return@map null
        val schoolName = prefs[SCHOOL_NAME] ?: return@map null
        val subjectsStr = prefs[SUBJECTS] ?: return@map null
        val rollNumber = prefs[ROLL_NUMBER] ?: ""

        UserProfile(
            name = name,
            className = className,
            board = Board.valueOf(boardStr),
            schoolName = schoolName,
            subjects = subjectsStr.split(",").filter { it.isNotEmpty() }.map { Subject.valueOf(it) },
            rollNumber = rollNumber
        )
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[NAME] = profile.name
            prefs[CLASS_NAME] = profile.className
            prefs[BOARD] = profile.board.name
            prefs[SCHOOL_NAME] = profile.schoolName
            prefs[SUBJECTS] = profile.subjects.joinToString(",") { it.name }
            prefs[ROLL_NUMBER] = profile.rollNumber
        }
    }
}
