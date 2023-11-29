package com.example.hltv.ui.screens.settingsScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var darkModeEnabled by remember {
        mutableStateOf(false)
    }
    Surface (modifier = Modifier.padding(16.dp)) {
        SettingsToggle(settingName = "Dark mode",
            isChecked = darkModeEnabled,
            onCheckedChange = { darkModeEnabled = it })

    }

}

@Composable
fun SettingsToggle(
    settingName: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
    ) {
    Row (modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        Text(text = settingName)

        Switch(checked = isChecked, onCheckedChange =onCheckedChange)
    }
}