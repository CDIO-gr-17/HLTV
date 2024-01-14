package com.example.hltv.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreSession @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val DATA = "Data"
        private const val FAVOURITETEAM = "favouriteTeam"
        val favouriteTeam = intPreferencesKey(FAVOURITETEAM)
    }

    suspend fun setFavouriteTeam(teamID: Int) {
        dataStore.edit { preference ->
            preference[favouriteTeam] = teamID
            }
    }
    fun getFavouriteTeam(): Flow<Int> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[favouriteTeam] ?: 0
        }
    }
}