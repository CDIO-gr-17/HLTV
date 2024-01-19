package com.example.hltv.ui.screens.homeScreen

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.hltv.HLTVApp
import com.example.hltv.ui.common.CommonCard
import org.junit.Rule
import org.junit.Test

class HomeScreenKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreenShowLiveMatchTest() {
        composeTestRule.setContent {
                //HLTVApp(prefDataKeyValueStore)
        }
        composeTestRule.onNodeWithText("Your match is live!").assertExists()
    }
    @Test
    fun alwaysTrueTest() {
        composeTestRule.setContent { CommonCard(modifier = Modifier ) {
            Text(text = "Your match is live!")
        } }
        composeTestRule.onNodeWithText("Your match is live!").assertExists()
    }

    @Test
    fun homeScreen() {
    }
}