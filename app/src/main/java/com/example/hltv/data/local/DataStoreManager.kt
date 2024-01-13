package com.example.hltv.data.local
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class DataStoreManager private constructor(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val FAVORITE_TEAM_ID_KEY = preferencesKey<Int>("favorite_team_id")

        @Volatile
        private var instance: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return instance ?: synchronized(this) {
                instance ?: DataStoreManager(context).also { instance = it }
            }
        }
    }

    suspend fun setFavoriteTeamId(teamId: Int) {
        dataStore.edit { preferences ->
            preferences[FAVORITE_TEAM_ID_KEY] = teamId
        }
    }

    val favoriteTeamId: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[FAVORITE_TEAM_ID_KEY] ?: -1
        }
}