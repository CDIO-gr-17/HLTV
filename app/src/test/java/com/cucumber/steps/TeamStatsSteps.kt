package com.cucumber.steps




import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.*
import kotlin.math.roundToInt

class TeamStatsSteps {
    lateinit var ages : MutableList<Int>
    var averageAge : Double = 0.0

    @Given("The players ages are {int} and {int} and {int} and {int} and {int}")
    fun the_players_ages_are_and_and_and_and(
        int1: Int,
        int2: Int,
        int3: Int,
        int4: Int,
        int5: Int
    ) {
        ages = mutableListOf(int1, int2, int3, int4, int5)
    }

    @When("I check the average age of the team")
    fun i_check_the_average_age_of_the_team() {
        val viewModel = SingleTeamViewModel()
        averageAge = viewModel.getAvgAgeFromTimestamp(ages)
    }

    @Then("I should see that the average age is {int}")
    fun i_should_see_that_the_average_age_is(int: Int) {
        assertEquals(int,averageAge.roundToInt())
    }





}