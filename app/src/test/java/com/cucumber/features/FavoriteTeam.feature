Feature: Get information about your favorite team

  Background:
    Given I am on the home page
      And I have a favorite team

  Scenario: Check match-results
    When I navigate to my favorite team
    Then I should see the results of the past matches for my favorite team

  Scenario: Check upcoming matches
    When I navigate to my favorite team
    Then I should see the upcoming matches for my favorite team