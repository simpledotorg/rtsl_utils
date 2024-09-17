Feature: Number of overdue patients called
  ___
  As a tester
  Given I have some tracked entity instances with call history in the system
  When  I export the analytics
  Then I should should see the "HTN - Overdue patients called" program indicator data for each month as expected
  ___

  Scenario: Called overdue patients should be counted
    Given I am signed in as a user with role "Superuser"
    And I have access to an organisation unit at level 5
    And I register that organisation unit for program "Hypertension & Diabetes"

    And I create a new TEI on "7_MonthsAgo" for this Facility with the following attributes
      | GEN - Given name                      | Rose         |
      | GEN - Family name                     | Mary         |
      | GEN - Sex                             | FEMALE       |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo_Minus_1_Day"
    And That TEI has a "Calling report" event on "6_MonthsAgo_Plus_1_Day" with following data
      | Result of call                          | REMIND_TO_CALL_LATER |
    And That TEI has a "Calling report" event on "3_MonthsAgo_Plus_1_Day" with following data
      | Result of call                          | REMOVE_FROM_OVERDUE |
      | HTN - Remove from overdue list because: | OTHER               |

    When I export the analytics

    # When we write the indicator name, we have to make sure it's the right indicator name without any typo
    Then The value of "PI":"HTN - Overdue patients called" with period type "Months" should be
      | 3_MonthsAgo | 1 |
      | 4_MonthsAgo | 0 |
      | 5_MonthsAgo | 0 |
      | 6_MonthsAgo | 1 |
      | 7_MonthsAgo | 0 |