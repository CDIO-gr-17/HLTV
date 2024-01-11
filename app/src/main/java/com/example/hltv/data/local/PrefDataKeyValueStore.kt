package com.example.hltv.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PrefDataKeyValueStore(val context: Context) {
    private object PreferenceKeys {
        val FAVOURITETEAM = intPreferencesKey("favouriteTeam")
    }

    suspend fun updateFavouriteTeam(teamID: Int) =
        context.dataStore.edit { preferences: MutablePreferences ->
            preferences[PreferenceKeys.FAVOURITETEAM] = teamID
        }

    fun getFavouriteTeam(): Flow<Int> = context.dataStore.data.map { preferences: Preferences ->
        preferences[PreferenceKeys.FAVOURITETEAM] ?: 0

    }
}