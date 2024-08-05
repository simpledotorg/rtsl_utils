Feature: Number of overdue patients
  ____
  As a tester
  Given I have a few tracked entity instances with overdue event
  When  I run the analytics
  Then  I should see the actual number of overdue patients under the program indicator "HTN - Overdue patients" for each month
  ____

  Scenario: All patients has to be hypertensive
    Given I create a new OrgUnit
    # could move BTS
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"
    Given I create a new Patient on "2024-01-16" for this Facility with the following attributes
      | GEN - Given name                      | Test         |
      | GEN - Family name                     | TEST         |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-16" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "2024-02-16"

    Given I create a new Patient on "2024-01-16" for this Facility with the following attributes
      | GEN - Given name                      | Test         |
      | GEN - Family name                     | TEST         |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | NO           |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-16" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "2024-02-16"

    When Export the analytics
    When Run the Hypertension data aggregation
    Then The value of PI "HTN - Overdue patients" should be
      | 202407 | 1 |
      | 202406 | 1 |
      | 202405 | 1 |
      | 202404 | 1 |
      | 202403 | 1 |
      | 202402 | 0 |
      | 202401 | 0 |

  Scenario: Dead patients should not be included
    Given I create a new OrgUnit
    # could move BTS
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"

    Given I create a new Patient on "2024-01-16" for this Facility with the following attributes
      | GEN - Given name                      | Test         |
      | GEN - Family name                     | TEST         |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-16" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "2024-02-16"

    Given I create a new Patient on "2024-01-16" for this Facility with the following attributes
      | GEN - Given name                      | Test         |
      | GEN - Family name                     | TEST         |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-16" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "2024-02-16"
    Given That patient was updated on "2024-06-02" with the following attributes
      | HTN - NCD Patient Status | DIED |
    When Export the analytics
    When Run the Hypertension data aggregation
    Then The value of PI "HTN - Overdue patients" should be
      | 202407 | 1 |
      | 202406 | 1 |
      | 202405 | 1 |
      | 202404 | 1 |
      | 202403 | 1 |
      | 202402 | 0 |
      | 202401 | 0 |


  Scenario: All patients should have a scheduled event with scheduled date in the past
    Given I create a new OrgUnit
    # could move BTS
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"
    Given I create a new Patient on "2024-01-16" for this Facility with the following attributes
      | GEN - Given name                      | Test         |
      | GEN - Family name                     | TEST         |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-16" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "2024-02-16"

    Given I create a new Patient on "2024-01-17" for this Facility with the following attributes
      | GEN - Given name                      | Test         |
      | GEN - Family name                     | TEST         |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-17" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    Given That patient has a "Calling report" event on "2024-03-17" with following data
      | Result of call | REMOVE_FROM_OVERDUE |
      | HTN - Remove from overdue list because:  | OTHER   |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-16" which was scheduled on "2024-03-16" with following data
      | Systole  | 142 |
      | Diastole | 95  |


    When Export the analytics
    When Run the Hypertension data aggregation
    Then The value of PI "HTN - Overdue patients" should be
      | 202407 | 1 |
      | 202406 | 1 |
      | 202405 | 2 |
      | 202404 | 2 |
      | 202403 | 1 |
      | 202402 | 0 |
      | 202401 | 0 |


