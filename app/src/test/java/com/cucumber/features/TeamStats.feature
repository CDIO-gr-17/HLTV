Feature: Check if the teamsstats are correct

  Scenario: Average age of team
    Given The players ages are 1007114400 and 1038650400 and 1070186400 and 1101808800 and 1133344800
    When  I check the average age of the team
    Then I should see that the average age is 20

