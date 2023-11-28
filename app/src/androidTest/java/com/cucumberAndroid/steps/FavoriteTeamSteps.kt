package com.cucumber.steps

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cucumberAndroid.support.TestEnvironment
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.runner.RunWith

class FavoriteTeamSteps {
    @Given("I am on the home page")
    fun i_am_on_the_home_page() {
        val composeTestRule = createComposeRule()
        composeTestRule.setContent { Text(text = "test") }


    }

    @When("I navigate to the matches page")
    fun i_navigate_to_the_matches_page() {
        TestEnvironment.composeTestRule.onNodeWithText("Matches").performClick()

    }

    @Then("I should see the matches that are live right now")
    fun i_should_see_the_matches_that_are_live_right_now() {
        TestEnvironment.composeTestRule.onNodeWithTag("LiveMatchCard").assertIsDisplayed()

    }


}




/*
var composeTestRule = activityScenarioRule<MainActivity>()
composeTestRule.scenario.onActivity {
    val navcontroller = remember
}

// Launch your activity using ActivityScenario
val scenario = ActivityScenario.launch(MainActivity::class.java)
scenario.onActivity {

}


scenario.onActivity { activity ->


    val navController = rememberNavController()

    composeTestRule.setContent {
        MainNavHost(navController = , modifier = )
    }
}
scenario

// Create your ComposeTestRule
// val composeTestRule = createAndroidComposeRule<MainActivity>()



composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
// TestEnvironment.composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
*/