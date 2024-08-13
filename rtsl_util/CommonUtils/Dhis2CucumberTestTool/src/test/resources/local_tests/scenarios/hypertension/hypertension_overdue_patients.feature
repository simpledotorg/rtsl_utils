Feature: Number of overdue patients
  ____
  As a tester
  Given I have a few tracked entity instances with overdue event
  When  I run the analytics and aggregated data
  Then  I should see the number of overdue patients under the program indicator "HTN - Overdue patients" for each month
  ____

  Scenario: All patients has to be hypertensive
    Given I create a new OrgUnit
    # could move BTS
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"
    Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"

    Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"
    When Export the analytics
    When Run the Hypertension data aggregation
    Then The value of "PI":"HTN - Overdue patients" with period type "Months" should be
      | thisMonth    | 1 |
      | 1_MonthAgo   | 1 |
      | 2_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 1 |
      | 4_MonthsAgo  | 1 |
      | 5_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 1 |
      | 7_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 9_MonthsAgo  | 0 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |

  Scenario: Dead patients should not be included
    Given I create a new OrgUnit
    # could move BTS
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"

    Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"

    Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"
    Given That patient was updated on "2_MonthsAgo" with the following attributes
      | HTN - NCD Patient Status | DIED |
    When Export the analytics
    When Run the Hypertension data aggregation
    Then The value of "PI":"HTN - Overdue patients" with period type "Months" should be
      | thisMonth    | 1 |
      | 1_MonthAgo   | 1 |
      | 2_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 1 |
      | 4_MonthsAgo  | 1 |
      | 5_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 1 |
      | 7_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 9_MonthsAgo  | 0 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |

  Scenario: All patients should have a scheduled event with scheduled date in the past
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"
    Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That patient has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"

    Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    Given That patient has a "Calling report" event on "5_MonthsAgo" with following data
      | Result of call                          | REMOVE_FROM_OVERDUE |
      | HTN - Remove from overdue list because: | OTHER               |
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" which was scheduled on "5_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    When Export the analytics
    When Run the Hypertension data aggregation
    Then The value of "PI":"HTN - Overdue patients" with period type "Months" should be
      | thisMonth    | 1 |
      | 1_MonthAgo   | 1 |
      | 2_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 1 |
      | 4_MonthsAgo  | 2 |
      | 5_MonthsAgo  | 2 |
      | 6_MonthsAgo  | 1 |
      | 7_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 9_MonthsAgo  | 0 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |
