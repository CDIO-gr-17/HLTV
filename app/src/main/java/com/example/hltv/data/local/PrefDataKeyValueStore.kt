package com.example.hltv.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PrefDataKeyValueStore private constructor(context: Context) {
    private object PreferenceKeys {
        val FAVOURITETEAM: Preferences.Key<Int> = intPreferencesKey("favouriteTeam")
    }

    private val applicationContext = context.applicationContext

    suspend fun updateFavouriteTeam(teamID: Int) =
        applicationContext.dataStore.edit { preferences: MutablePreferences ->
            preferences[PreferenceKeys.FAVOURITETEAM] = teamID
        }

    fun getFavouriteTeam(): Flow<Int> = applicationContext.dataStore.data
        .catchAndHandleError()
        .map { preferences: Preferences ->
            return@map preferences[PreferenceKeys.FAVOURITETEAM] ?: 0
        }

    private fun Flow<Preferences>.catchAndHandleError(): Flow<Preferences> =
        this.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    companion object {
        @Volatile
        private var INSTANCE: PrefDataKeyValueStore? = null

        fun getInstance(context: Context): PrefDataKeyValueStore =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PrefDataKeyValueStore(context.applicationContext).also { INSTANCE = it }
            }
    }
}