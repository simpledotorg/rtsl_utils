Feature: Number of overdue patients
  ___
  As a tester
  Given Some managed overdue HTN TEIs return to care after being called
  When  I export the analytics
  Then I should see data in the "HTN - Overdue patients called and returned to care" program indicator as expected
  ___

  Scenario: Overdue patients called returned to care
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
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"
    And That TEI has a "Calling report" event on "5_MonthsAgo_Plus_1_Day" with following data
      | Result of call                          | AGREE_TO_VISIT |
    And That TEI has a "Hypertension & Diabetes visit" event on "5_MonthsAgo_Plus_10_Day" with following data
      | Systole  | 142 |
      | Diastole | 95  |

    When I export the analytics

    Then The value of "PI":"HTN - Overdue patients called and returned to care" with period type "Months" should be
      | 4_MonthsAgo  | 0 |
      | 5_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 0 |

  Scenario: Overdue patients called did not return to care
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
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"
    And That TEI has a "Calling report" event on "5_MonthsAgo_Plus_1_Day" with following data
      | Result of call                          | AGREE_TO_VISIT |

    When I export the analytics

    Then The value of "PI":"HTN - Overdue patients called and returned to care" with period type "Months" should be
      | 4_MonthsAgo  | 0 |
      | 5_MonthsAgo  | 0 |
      | 6_MonthsAgo  | 0 |

  Scenario: Overdue patients returned to care without being called
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
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"
    And That TEI has a "Hypertension & Diabetes visit" event on "5_MonthsAgo_Plus_10_Day" with following data
      | Systole  | 142 |
      | Diastole | 95  |

    When I export the analytics

    Then The value of "PI":"HTN - Overdue patients called and returned to care" with period type "Months" should be
      | 3_MonthsAgo  | 0 |
      | 4_MonthsAgo  | 0 |
      | 5_MonthsAgo  | 0 |
      | 6_MonthsAgo  | 0 |
      | 7_MonthsAgo  | 0 |

  Scenario: Overdue patients returned to care after 15 days
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
      | District                              | KOLARA       |
      | HTN - Consent to record data          | true         |
      | HTN - NCD Patient Status              | ACTIVE       |
    And That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    And That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"
    And That TEI has a "Calling report" event on "6_MonthsAgo_Plus_5_Day" with following data
      | Result of call                          | AGREE_TO_VISIT |
    And That TEI has a "Hypertension & Diabetes visit" event on "5_MonthsAgo_Plus_1_Day" with following data
      | Systole  | 142 |
      | Diastole | 95  |

    When I export the analytics

    Then The value of "PI":"HTN - Overdue patients called and returned to care" with period type "Months" should be
      | 4_MonthsAgo  | 0 |
      | 5_MonthsAgo  | 0 |
      | 6_MonthsAgo  | 0 |

