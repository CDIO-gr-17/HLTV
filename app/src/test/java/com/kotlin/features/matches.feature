Feature: Matches

  Scenario: User checks recent matchresult for favorite team
    Given I am on the home page
      And I have a favorite team
    When I click on my favorite team
      And I click on the "Past Matches" link
    Then I should see the results og the past matches for favorite team

  Scenario: User checks the upcoming matches for their favorite team
    Given I am on the home page
      And I have a favorite team
    When I click on my favorite team
      And I click on the "Upcoming Matches" link
    Then I should see the upcoming matches for favorite team