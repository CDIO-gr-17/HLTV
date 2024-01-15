@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.hltv

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.HLTVTheme
import com.example.hltv.data.local.PrefDataKeyValueStore
import com.example.hltv.data.remote.getEvent
import com.example.hltv.data.remote.getPlayerFromPlayerID
import com.example.hltv.data.remote.getTeamNameFromID
import com.example.hltv.navigation.Destination
import com.example.hltv.navigation.Home
import com.example.hltv.navigation.MainNavHost
import com.example.hltv.navigation.Matches
import com.example.hltv.navigation.Settings
import com.example.hltv.navigation.SingleMatch
import com.example.hltv.navigation.SinglePlayer
import com.example.hltv.navigation.SingleTeam
import com.example.hltv.navigation.allAppScreens
import com.example.hltv.navigation.bottomAppBarScreens
import com.example.hltv.ui.screens.matchesScreen.DatePicker
import com.example.hltv.ui.screens.singleTeamScreen.FavoriteButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Initialize the singleton instance in your Application class or where appropriate
            HLTVApp(applicationContext)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HLTVApp(context: Context) {
    HLTVTheme {
        val prefDataKeyValueStore = PrefDataKeyValueStore.getInstance(context)
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val canNavigateBack = !bottomAppBarScreens.any { it.route == currentDestination?.route }
        val currentScreen =
            allAppScreens.find { currentDestination?.route?.startsWith(it.route) ?: false } ?: Home



        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                title = {
                    setTopAppBarTitle(currentScreen, currentBackStack)
                },
                navigationIcon = if (canNavigateBack) {
                    {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back-Arrow"
                            )
                        }
                    }
                } else {
                    {}
                },
                actions = {
                    if (currentScreen == SingleTeam)
                        FavoriteButton(
                            prefDataKeyValueStore,
                            currentBackStack?.arguments?.getString("teamID")?.toInt()!!
                        )
                    else if (currentScreen == Matches)
                        DatePicker(context = context)
                    else {
                        IconButton(
                            onClick = { navController.navigate(Settings.route) },
                            enabled = currentScreen != Settings
                        ) {
                            Icon(
                                imageVector = if (currentScreen != Settings) Icons.Outlined.Settings else Icons.Default.Settings,
                                contentDescription = "Settings Icon",
                                tint = MaterialTheme.colorScheme.onSurface
                            )

                    }
                }},

            )
        }, bottomBar = {
            NavigationBar {
                bottomAppBarScreens.forEach { item ->
                    NavigationBarItem(selected = currentScreen == item,
                        onClick = { navController.navigate(item.route) },
                        enabled = item != currentScreen,
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = item.icon),
                                contentDescription = item.route + "Icon",
                            )
                        },
                        label = { Text(text = item.route) })
                }
            }
        }) {
            MainNavHost(
                navController = navController, modifier = Modifier.padding(it)
            )
        }
    }


}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun setTopAppBarTitle(currentScreen: Destination, currentBackStack: NavBackStackEntry?) {
    if (currentScreen == SingleTeam) {
        var topAppBarTitle by remember { mutableStateOf("Team info") }
        CoroutineScope(Dispatchers.IO).launch {
            val teamID = currentBackStack?.arguments?.getString("teamID")
            val teamIDInt = teamID?.toInt()
            if (teamIDInt != null) {
                topAppBarTitle = getTeamNameFromID(teamIDInt)!!
            }
        }
        Text(text = topAppBarTitle)
    } else if (currentScreen == SingleMatch) {
        var topAppBarTitle by remember { mutableStateOf("Match info") }
        CoroutineScope(Dispatchers.IO).launch {
            val matchID = currentBackStack?.arguments?.getString("matchID")
            val matchIDInt = matchID?.toInt()
            if (matchIDInt != null) {
                val event = getEvent(matchIDInt).event
                Log.d("Topbar", "Got event")
                val homeTeamName = event!!.homeTeam.shortName
                val awayTeamName = event.awayTeam.shortName
                topAppBarTitle = "$homeTeamName vs $awayTeamName"
            }
        }
        Text(text = topAppBarTitle, textAlign = TextAlign.Center)
    } else if (currentScreen == SinglePlayer) {
        var topAppBarTitle by remember { mutableStateOf("Player info") }
        CoroutineScope(Dispatchers.IO).launch {
            val playerID = currentBackStack?.arguments?.getString("playerID")
            val playerIDInt = playerID?.toInt()
            if (playerID != null) {
                val player = getPlayerFromPlayerID(playerIDInt).player
                Log.d("Topbar","Got player")
                topAppBarTitle = player.name.toString()
            }
        }
        Text(text = topAppBarTitle)
    } else {
        Text(text = currentScreen.route)
    }

}
