package com.example.hltv.ui.screens.settingsScreen

import android.graphics.Bitmap
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

    fun loadData (dataStore : PrefDataKeyValueStore){
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.getFavouriteTeam().collect() { int ->
                _favoriteTeam.value = int}
            _favoriteTeamName.value = getTeamNameFromID(_favoriteTeam.value).toString()
            _teamLogo.value = getTeamImage(_favoriteTeam.value)
        }
    }

}