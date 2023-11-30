package com.cucumber.steps




import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.*
import java.math.RoundingMode
import java.text.DecimalFormat
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

    fun getAvgAgeFromTimestamp(dateOfBirthTimestampList: MutableList<Int>): Double {            //TODO: This should be moved to a more appropriate place
        var totalAgeOfPlayers: Long = 0
        for (dateOfBirthTimestamp in dateOfBirthTimestampList) {
            totalAgeOfPlayers += ((System.currentTimeMillis() // Subtracts the current time in milliseconds from the players date of birth in milliseconds
                    - (dateOfBirthTimestamp.toLong() * 1000)))
        }
        if(dateOfBirthTimestampList.size!=0) {
            val avgAgeOfPlayersInMillis: Long = totalAgeOfPlayers / dateOfBirthTimestampList.size
            val df = DecimalFormat("#.#")
            val avgAgeOfPlayersInYears = avgAgeOfPlayersInMillis/365.25/3600/24/1000
            df.roundingMode = RoundingMode.CEILING
            print(avgAgeOfPlayersInYears.toDouble())
            return avgAgeOfPlayersInYears.toDouble()

            // String.format("%.1f", TimeUnit.MILLISECONDS.toDays(avgAgeOfPlayersInMillis) / 365.25).toDouble()

        }
        else return 0.0
    }



}