package com.example.hltv

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LiveMatchesUseCaseTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var navcontroller : NavHostController


    @Before
    fun setup(){
        composeTestRule.setContent {
            HLTVApp(prefDataKeyValueStore)
        }
    }

    @Test
    fun iAmOnTheHomePage(){
        composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun iNavigateToTheMatchesPage(){
        composeTestRule.onNodeWithText("Matches").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Your match is live!").assertIsDisplayed()

    }
}