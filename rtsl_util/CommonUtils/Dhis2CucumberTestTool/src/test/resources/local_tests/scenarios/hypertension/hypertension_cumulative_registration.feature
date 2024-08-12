@programIndicator
Feature: Audit HTN - Cumulative registrations


  Scenario: All the tracked entity instances except the dead ones are included
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"

    #
    # Patient 1 - Dead patient
    #
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
    Given That patient has a "Hypertension & Diabetes" event on "2024-01-16" with following data
      | Systole  | 145 |
      | Diastole | 92  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-01-16" with following data
      | Systole  | 145 |
      | Diastole | 92  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-02-02" with following data
      | Systole  | 140 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-05-02" with following data
      | Systole  | 136 |
      | Diastole | 84  |
    Given That patient was updated on "2024-06-02" with the following attributes
      | HTN - NCD Patient Status | DIED |

    #
    # Patient 2 - Not a hypertension patient
    #
    Given I create a new Patient on "2024-01-16" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes" event on "2024-01-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 130      |
      | HTN - Blood sugar unit          | MG_OR_DL |

    #
    # Patient 3
    #
    Given I create a new Patient on "2023-10-20" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes" event on "2023-10-20" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes" event on "2023-11-27" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-01-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-02-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-03-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    #
    # Patient 4
    #
    Given I create a new Patient on "2023-10-20" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes" event on "2023-10-20" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    Given That patient has a "Hypertension & Diabetes" event on "2023-11-27" with following data
      | Systole  | 139 |
      | Diastole | 90  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-01-04" with following data
      | Systole  | 132 |
      | Diastole | 82  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-02-07" with following data
      | Systole  | 128 |
      | Diastole | 78  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-03-03" with following data
      | Systole  | 140 |
      | Diastole | 88  |

    When Export the analytics
    When Run the Hypertension data aggregation

#    When Run the Hypertension data aggregation. we could add this logic to indicator checks

    Then The value of "PI" "HTN - Cumulative registrations" should be
      | 202407 | 2 |
      | 202406 | 2 |
      | 202405 | 2 |
      | 202404 | 2 |
      | 202403 | 2 |
      | 202402 | 2 |
      | 202401 | 2 |
      | 202312 | 2 |
      | 202311 | 2 |
      | 202310 | 2 |
      | 202309 | 0 |
      | 202308 | 0 |

    Then The value of "Data Element" "HTN - Cumulative registrations" should be
      | 202407 | 2 |
      | 202406 | 2 |
      | 202405 | 2 |
      | 202404 | 2 |
      | 202403 | 2 |
      | 202402 | 2 |
      | 202401 | 2 |
      | 202312 | 2 |
      | 202311 | 2 |
      | 202310 | 2 |
      | 202309 | 0 |
      | 202308 | 0 |

    Then The value of "PI" "HTN - Cumulative dead patients" should be
      | 202407 | 1 |
      | 202406 | 1 |
      | 202405 | 1 |
      | 202404 | 1 |
      | 202403 | 1 |
      | 202402 | 1 |
      | 202401 | 1 |
      | 202312 | 0 |
      | 202311 | 0 |
      | 202310 | 0 |
      | 202309 | 0 |
      | 202308 | 0 |

  Scenario: Only hypertensive tracked entity instances are included
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"

    #
    # Patient 1 - Not a hypertension patient
    #
    Given I create a new Patient on "2024-01-16" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes" event on "2024-01-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 130      |
      | HTN - Blood sugar unit          | MG_OR_DL |

    #
    # Patient 2
    #
    Given I create a new Patient on "2023-10-20" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes" event on "2023-10-20" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes" event on "2023-11-27" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-01-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-02-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-03-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    #
    # Patient 3
    #
    Given I create a new Patient on "2023-10-20" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes" event on "2023-10-20" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    Given That patient has a "Hypertension & Diabetes" event on "2023-12-27" with following data
      | Systole  | 139 |
      | Diastole | 90  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-02-04" with following data
      | Systole  | 132 |
      | Diastole | 82  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-05-07" with following data
      | Systole  | 128 |
      | Diastole | 78  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-07-03" with following data
      | Systole  | 140 |
      | Diastole | 88  |

    When Export the analytics

    When Run the Hypertension data aggregation

    Then The value of "PI" "HTN - Cumulative registrations" should be
      | 202407 | 2 |
      | 202406 | 2 |
      | 202405 | 2 |
      | 202404 | 2 |
      | 202403 | 2 |
      | 202402 | 2 |
      | 202401 | 2 |
      | 202312 | 2 |
      | 202311 | 2 |
      | 202310 | 2 |
      | 202309 | 0 |
      | 202308 | 0 |

  Scenario: Patients inactive for more than 12 months should be included
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"

    #
    # Patient 1 - Lost To Followup
    #
    Given I create a new Patient on "2023-01-16" for this Facility with the following attributes
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

    Given That patient has a "Hypertension & Diabetes" event on "2023-01-16" with following data
      | Systole  | 145 |
      | Diastole | 92  |
    Given That patient has a "Hypertension & Diabetes" event on "2023-01-16" with following data
      | Systole  | 145 |
      | Diastole | 92  |
    Given That patient has a "Hypertension & Diabetes" event on "2023-02-02" with following data
      | Systole  | 140 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes" event on "2023-05-02" with following data
      | Systole  | 136 |
      | Diastole | 84  |

    #
    # Patient 2 - Lost To Followup and Dead
    #
    Given I create a new Patient on "2023-12-20" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes" event on "2023-12-20" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-01-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-02-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-03-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-01-04" with following data
      | Systole  | 147 |
      | Diastole | 87  |
    Given That patient was updated on "2024-02-04" with the following attributes
      | HTN - NCD Patient Status | DIED |

    #
    # Patient 3 - Under care
    #
    Given I create a new Patient on "2023-10-20" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes" event on "2023-10-20" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes" event on "2023-12-27" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-02-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-04-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes" event on "2024-06-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    Given Export the analytics

    Given Run the Hypertension data aggregation

    Then The value of "PI" "HTN - Cumulative registrations" should be
      | 202407 | 2 |
      | 202406 | 2 |
      | 202405 | 2 |
      | 202404 | 2 |
      | 202403 | 2 |
      | 202402 | 2 |
      | 202401 | 2 |
      | 202312 | 2 |
      | 202311 | 2 |
      | 202310 | 2 |
      | 202309 | 1 |
      | 202308 | 1 |
