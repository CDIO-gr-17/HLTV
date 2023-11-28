package com.cucumberAndroid.support

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.hltv.navigation.MainNavHost

object TestEnvironment {
    var composeTestRule = createComposeRule()

    init {
        composeTestRule.setContent {
            val navcontroller = rememberNavController()
            MainNavHost(navController = navcontroller, modifier = Modifier)
        }
    }
}