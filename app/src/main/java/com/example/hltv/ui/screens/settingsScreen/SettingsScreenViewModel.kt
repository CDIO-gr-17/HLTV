package com.example.hltv.ui.screens.settingsScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.local.PrefDataKeyValueStore
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.data.remote.getTeamNameFromID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsScreenViewModel() : ViewModel() {
    private val _favoriteTeam = MutableStateFlow(0)
    val favoriteTeam: StateFlow<Int> = _favoriteTeam

    private val _favoriteTeamName = MutableStateFlow("")
    val favoriteTeamName: StateFlow<String> = _favoriteTeamName

    private val _teamLogo = MutableStateFlow<Bitmap?>(null)
    val teamLogo: StateFlow<Bitmap?> = _teamLogo

    var favoriteTeamOnHomeScreen: MutableStateFlow<Boolean> = MutableStateFlow(true)
    fun loadData(dataStore: PrefDataKeyValueStore) {
        viewModelScope.launch(Dispatchers.IO){
            dataStore.getHomepagePreference().collect() { boolean ->
                favoriteTeamOnHomeScreen.value = boolean
                return@collect
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.getFavouriteTeam().collect { int ->
                _favoriteTeam.value = int
                Log.d("SettingsScreenViewModel", "Favourite team is now: ${_favoriteTeam.value}")
                if (_favoriteTeam.value == 0) {
                    return@collect
                }
                _favoriteTeamName.value = getTeamNameFromID(_favoriteTeam.value).toString()
                Log.d(
                    "SettingsScreenViewModel",
                    "Favourite team name is now: ${_favoriteTeamName.value}"
                )

                _teamLogo.value = getTeamImage(_favoriteTeam.value)
            }
        }
    }

    fun setFavoriteTeamPreference(dataStore: PrefDataKeyValueStore, value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.updateHomepagePreference(value)
        }
    }
}