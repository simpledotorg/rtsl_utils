Feature: Contactable Overdue Patients
  ___
  As a tester
  Given I have some "overdue" tracked entity instances with contact information in the system
  When I export analytics
  Then I should see the "HTN - Contactable overdue patients" program indicator data as expected
  ___

  Scenario: All patients must have a patient phone number
    Given I am signed in as a user with role "Superuser"
    And I have access to an organisation unit at level 5
    And I register that organisation unit for program "Hypertension & Diabetes"

    Given I create a new TEI on "7_MonthsAgo" for this Facility with the following attributes
      | GEN - Given name                      | Callable     |
      | GEN - Family name                     | Patient      |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | Patient Phone Number                  | 345672781624 |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"

    And I create a new TEI on "7_MonthsAgo" for this Facility with the following attributes
      | GEN - Given name                      | Unreachable  |
      | GEN - Family name                     | Patient      |
      | GEN - Sex                             | MALE         |
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
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"

    When wait for 1 second
    And I export the analytics

    Then The value of "PI":"HTN - Contactable overdue patients" with period type "Months" should be
      | thisMonth    | 1 |
      | 1_MonthAgo   | 1 |
      | 2_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 1 |
      | 4_MonthsAgo  | 1 |
      | 5_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 0 |
      | 7_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 9_MonthsAgo  | 0 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |

  Scenario: All patients has to be hypertensive
    Given I am signed in as a user with role "Superuser"
    And I have access to an organisation unit at level 5
    And I register that organisation unit for program "Hypertension & Diabetes"

    Given I create a new TEI on "7_MonthsAgo" for this Facility with the following attributes
      | GEN - Given name                      | Priyanka     |
      | GEN - Family name                     | Chopra       |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | Patient Phone Number                  | 345672781624 |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"

    And I create a new TEI on "7_MonthsAgo" for this Facility with the following attributes
      | GEN - Given name                      | Alia         |
      | GEN - Family name                     | Bhatt        |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | NO           |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | Patient Phone Number                  | 345672781624 |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"

    When wait for 1 second
    And I export the analytics

    Then The value of "PI":"HTN - Contactable overdue patients" with period type "Months" should be
      | thisMonth    | 1 |
      | 1_MonthAgo   | 1 |
      | 2_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 1 |
      | 4_MonthsAgo  | 1 |
      | 5_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 0 |
      | 7_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 9_MonthsAgo  | 0 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |

  Scenario: Dead patients should not be included
    Given I am signed in as a user with role "Superuser"
    And I have access to an organisation unit at level 5
    And I register that organisation unit for program "Hypertension & Diabetes"

    And I create a new TEI on "7_MonthsAgo" for this Facility with the following attributes
      | GEN - Given name                      | Test         |
      | GEN - Family name                     | TEST         |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | Patient Phone Number                  | 345672781624 |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"

    And I create a new TEI on "7_MonthsAgo" for this Facility with the following attributes
      | GEN - Given name                      | Test         |
      | GEN - Family name                     | TEST         |
      | GEN - Sex                             | MALE         |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | Patient Phone Number                  | 345672781624 |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"
    And That TEI was updated on "2_MonthsAgo" with the following attributes
      | HTN - NCD Patient Status | DIED |

    When wait for 1 second
    And I export the analytics

    Then The value of "PI":"HTN - Contactable overdue patients" with period type "Months" should be
      | thisMonth    | 1 |
      | 1_MonthAgo   | 1 |
      | 2_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 1 |
      | 4_MonthsAgo  | 1 |
      | 5_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 0 |
      | 7_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 9_MonthsAgo  | 0 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |

  Scenario: TEI are not marked as overdue anymore if they have visited after the due date.
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
      | Patient Phone Number                  | 345672781624 |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo_Minus_1_Day"

    And I create a new TEI on "7_MonthsAgo" for this Facility with the following attributes
      | GEN - Given name                      | Angel        |
      | GEN - Family name                     | John         |
      | GEN - Sex                             | FEMALE       |
      | HTN - Does patient have hypertension? | YES          |
      | HTN - Does patient have diabetes?     | YES          |
      | GEN - Date of birth                   | 32           |
      | Address (current)                     | Rose Gardens |
      | Patient Phone Number                  | 345672781624 |
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Calling report" event on "5_MonthsAgo_Plus_1_Day" with following data
      | Result of call                          | REMOVE_FROM_OVERDUE |
      | HTN - Remove from overdue list because: | OTHER               |
    And That TEI has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" which was scheduled on "5_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |

    When wait for 1 second
    And I export the analytics

    Then The value of "PI":"HTN - Contactable overdue patients" with period type "Months" should be
      | thisMonth    | 1 |
      | 1_MonthAgo   | 1 |
      | 2_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 2 |
      | 4_MonthsAgo  | 2 |
      | 5_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 1 |
      | 7_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 9_MonthsAgo  | 0 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |
