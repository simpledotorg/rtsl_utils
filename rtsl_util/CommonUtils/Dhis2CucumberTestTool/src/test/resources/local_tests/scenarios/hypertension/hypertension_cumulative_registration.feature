@programIndicator
Feature: Audit HTN - Cumulative registrations


  Scenario: All the tracked entity instances except the dead ones are included
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"

    #
    # Patient 1 - Dead patient
    #
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
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" with following data
      | Systole  | 136 |
      | Diastole | 84  |
    Given That patient was updated on "2_MonthsAgo" with the following attributes
      | HTN - NCD Patient Status | DIED |

    #
    # Patient 2 - Not a hypertension patient
    #
    Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
      | Given name         | Fabian       |
      | Family name        | Moore        |
      | Sex                | MALE         |
      | HTN diagnosis      | No           |
      | DM diagnosis       | YES          |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 130      |
      | HTN - Blood sugar unit          | MG_OR_DL |

    #
    # Patient 3
    #
    Given I create a new Patient on "9_MonthsAgo" for this Facility with the following attributes
      | Given name         | Sue          |
      | Family name        | Perkins      |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | v4DnYfXn9Mu        | YES          |
      | NI0QRzJvQ0k        | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "9_MonthsAgo" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "8_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    #
    # Patient 4
    #
    Given I create a new Patient on "9_MonthsAgo" for this Facility with the following attributes
      | Given name         | Kiran        |
      | Family name        | Kishor       |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | v4DnYfXn9Mu        | YES          |
      | NI0QRzJvQ0k        | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "9_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    Given That patient has a "Hypertension & Diabetes visit" event on "8_MonthsAgo" with following data
      | Systole  | 139 |
      | Diastole | 90  |
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 132 |
      | Diastole | 82  |
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 128 |
      | Diastole | 78  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 88  |

    When Export the analytics
    When Run the Hypertension data aggregation

#    When Run the Hypertension data aggregation. we could add this logic to indicator checks

    Then The value of "PI":"HTN - Cumulative registrations" with period type "Months" should be
      | thisMonth    | 2 |
      | 1_MonthAgo   | 2 |
      | 2_MonthsAgo  | 2 |
      | 3_MonthsAgo  | 2 |
      | 4_MonthsAgo  | 2 |
      | 5_MonthsAgo  | 2 |
      | 6_MonthsAgo  | 2 |
      | 7_MonthsAgo  | 2 |
      | 8_MonthsAgo  | 2 |
      | 9_MonthsAgo  | 2 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |

    Then The value of "PI":"HTN - Cumulative dead patients" with period type "Months" should be
      | thisMonth    | 1 |
      | 1_MonthAgo   | 1 |
      | 2_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 1 |
      | 4_MonthsAgo  | 1 |
      | 5_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 1 |
      | 7_MonthsAgo  | 1 |
      | 8_MonthsAgo  | 0 |
      | 9_MonthsAgo  | 0 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |

  Scenario: Only hypertensive tracked entity instances are included
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"

    #
    # Patient 1 - Not a hypertension patient
    #
    Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
      | Given name         | Fabian       |
      | Family name        | Moore        |
      | Sex                | MALE         |
      | HTN diagnosis      | No           |
      | DM diagnosis       | YES          |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 130      |
      | HTN - Blood sugar unit          | MG_OR_DL |

    #
    # Patient 2
    #
    Given I create a new Patient on "10_MonthsAgo" for this Facility with the following attributes
      | Given name         | Sue          |
      | Family name        | Perkins      |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | v4DnYfXn9Mu        | YES          |
      | NI0QRzJvQ0k        | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "10_MonthsAgo" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "9_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    #
    # Patient 3
    #
    Given I create a new Patient on "10_MonthsAgo" for this Facility with the following attributes
      | Given name         | Kiran        |
      | Family name        | Kishor       |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | v4DnYfXn9Mu        | YES          |
      | NI0QRzJvQ0k        | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "10_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    Given That patient has a "Hypertension & Diabetes visit" event on "8_MonthsAgo" with following data
      | Systole  | 139 |
      | Diastole | 90  |
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 132 |
      | Diastole | 82  |
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" with following data
      | Systole  | 128 |
      | Diastole | 78  |
    Given That patient has a "Hypertension & Diabetes visit" event on "1_MonthAgo" with following data
      | Systole  | 140 |
      | Diastole | 88  |

    When Export the analytics

    When Run the Hypertension data aggregation

    Then The value of "PI":"HTN - Cumulative registrations" with period type "Months" should be
      | thisMonth    | 2 |
      | 1_MonthAgo   | 2 |
      | 2_MonthsAgo  | 2 |
      | 3_MonthsAgo  | 2 |
      | 4_MonthsAgo  | 2 |
      | 5_MonthsAgo  | 2 |
      | 6_MonthsAgo  | 2 |
      | 7_MonthsAgo  | 2 |
      | 8_MonthsAgo  | 2 |
      | 9_MonthsAgo  | 2 |
      | 10_MonthsAgo | 2 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |

  Scenario: Patients inactive for more than 12 months should be included
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"

    #
    # Patient 1 - Lost To Followup
    #
    Given I create a new Patient on "18_MonthsAgo" for this Facility with the following attributes
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

    Given That patient has a "Hypertension & Diabetes visit" event on "18_MonthsAgo" with following data
      | Systole  | 145 |
      | Diastole | 92  |
    Given That patient has a "Hypertension & Diabetes visit" event on "17_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "14_MonthsAgo" with following data
      | Systole  | 136 |
      | Diastole | 84  |

    #
    # Patient 2 - Lost To Followup and Dead
    #
    Given I create a new Patient on "8_MonthsAgo" for this Facility with the following attributes
      | Given name         | Sue          |
      | Family name        | Perkins      |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | v4DnYfXn9Mu        | YES          |
      | NI0QRzJvQ0k        | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "8_MonthsAgo" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 134 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 147 |
      | Diastole | 87  |
    Given That patient was updated on "6_MonthsAgo" with the following attributes
      | HTN - NCD Patient Status | DIED |

    #
    # Patient 3 - Under care
    #
    Given I create a new Patient on "10_MonthsAgo" for this Facility with the following attributes
      | Given name         | Sue          |
      | Family name        | Perkins      |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | v4DnYfXn9Mu        | YES          |
      | NI0QRzJvQ0k        | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "10_MonthsAgo" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "8_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "4_MonthsAgo" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2_MonthsAgo" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    Given Export the analytics

    Given Run the Hypertension data aggregation

    Then The value of "Program Indicator":"HTN - Cumulative registrations" with period type "Months" should be
      | thisMonth    | 2 |
      | 1_MonthAgo   | 2 |
      | 2_MonthsAgo  | 2 |
      | 3_MonthsAgo  | 2 |
      | 4_MonthsAgo  | 2 |
      | 5_MonthsAgo  | 2 |
      | 6_MonthsAgo  | 2 |
      | 7_MonthsAgo  | 2 |
      | 8_MonthsAgo  | 2 |
      | 9_MonthsAgo  | 2 |
      | 10_MonthsAgo | 2 |
      | 11_MonthsAgo | 1 |
      | 12_MonthsAgo | 1 |
