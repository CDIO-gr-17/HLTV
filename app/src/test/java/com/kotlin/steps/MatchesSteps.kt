package com.kotlin.steps

import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.*


class MatchesSteps {

    @Given("I am on the home page")
    fun i_am_on_the_home_page() {

        throw PendingException()
    }

    @Given("I have a favorite team")
    fun i_have_a_favorite_team() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @When("I click on my favorite team")
    fun i_click_on_my_favorite_team() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @When("I click on the {string} link")
    fun i_click_on_the_link(string: String?) {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @Then("I should see the results og the past matches for favorite team")
    fun i_should_see_the_results_og_the_past_matches_for_favorite_team() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @Then("I should see the upcoming matches for favorite team")
    fun i_should_see_the_upcoming_matches_for_favorite_team() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }



}