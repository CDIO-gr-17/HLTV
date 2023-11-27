Feature: Get information about your favorite team

  Background:
    Given I am on the home page

  Scenario: Check live matches
    When I navigate to the matches page
    Then I should see the matches that are live right now

  Scenario: Check upcoming matches
    When I navigate to my favorite team
    Then I should see the upcoming matches for my favorite team