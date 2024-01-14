package com.example.hltv.ui.screens.settingsScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hltv.R
import com.example.hltv.data.local.PrefDataKeyValueStore
import com.example.hltv.ui.common.CommonCard

@Composable
fun SettingsScreen(onClickSingleTeam: (String?) -> Unit) {
    val dataStore = PrefDataKeyValueStore.getInstance(LocalContext.current)
    val viewModel: SettingsScreenViewModel = viewModel()
    viewModel.loadData(dataStore)

    val teamName by viewModel.favoriteTeamName.collectAsState()
    val teamLogo by viewModel.teamLogo.collectAsState()

    var darkModeEnabled by remember {
        mutableStateOf(false)
    }
    var favoriteTeamOnHomeScreen by remember {
        mutableStateOf(true)
    }

    Column {
        favoriteTeamSection(teamName = teamName, teamLogo = teamLogo) {
            SettingsToggle(
                settingName = "Show team-info on homescreen",
                isChecked = favoriteTeamOnHomeScreen,
                onCheckedChange = { favoriteTeamOnHomeScreen = it })
        }

        Surface(modifier = Modifier.padding(16.dp)) {
            SettingsToggle(settingName = "Dark mode",
                isChecked = darkModeEnabled,
                onCheckedChange = { darkModeEnabled = it })

        }
    }
}

@Composable
private fun favoriteTeamSection(
    teamName: String,
    teamLogo: Bitmap?,
    bottomBox: @Composable () -> Unit
) {
    CommonCard(
        modifier = Modifier,
        topBox = {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(
                        text = if (teamName == "") "You have no favorite Team"
                        else "Favorite Team: $teamName",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(
                            top = 16.dp,
                            bottom = 4.dp,
                            start = 8.dp,
                            end = 8.dp
                        )
                    )
                    Text(
                        text = "Select a new favorite team by searching",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                val stdBitmap = BitmapFactory.decodeResource(
                    LocalContext.current.resources, R.drawable.questionmark
                ).asImageBitmap()
                Image(
                    bitmap = if (teamLogo != null) teamLogo!!.asImageBitmap() else stdBitmap,
                    contentDescription = "Team Logo",
                    modifier = Modifier
                        .height(70.dp)
                )
            }
        },
        bottomBox ={bottomBox()}
    )

}

@Composable
fun SettingsToggle(
    settingName: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Text(text = settingName)
        Spacer(modifier = Modifier.weight(1f))

        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}