Feature: Audit the program indicator: HTN - Cumulative registrations


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

    Given That patient visited for Hypertension on "2024-01-16" with Blood Pressure reading 145:92
    Given That patient visited for Hypertension on "2024-01-16" with Blood Pressure reading 145:92
    Given That patient visited for Hypertension on "2024-02-02" with Blood Pressure reading 140:87
    Given That patient visited for Hypertension on "2024-05-02" with Blood Pressure reading 136:84
    Given Update that patient with the following attributes
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
    Given That patient visited for Diabetes on "2023-10-20" with Blood Sugar type "FBS" and reading 130 "MG_OR_DL"

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
    Given That patient visited for Hypertension on "2023-10-20" with Blood Pressure reading 147:89
    Given That patient visited for Hypertension on "2023-11-27" with Blood Pressure reading 142:87
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 140:83
    Given That patient visited for Hypertension on "2024-02-07" with Blood Pressure reading 135:84
    Given That patient visited for Hypertension on "2024-03-03" with Blood Pressure reading 134:83

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
    Given That patient visited for Hypertension on "2023-10-20" with Blood Pressure reading 142:95
    Given That patient visited for Hypertension on "2023-11-27" with Blood Pressure reading 139:90
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 132:82
    Given That patient visited for Hypertension on "2024-02-07" with Blood Pressure reading 128:78
    Given That patient visited for Hypertension on "2024-03-03" with Blood Pressure reading 140:88

    When Export the analytics

    When Run the Hypertension data aggregation

    Then The value of PI "HTN - Cumulative registrations" should be
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

    Then The value of PI "HTN - Cumulative dead patients" should be
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

  Scenario: Audit the program indicator: HTN - Cumulative registrations
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

    Given That patient visited for Hypertension on "2024-01-16" with Blood Pressure reading 145:92
    Given That patient visited for Hypertension on "2024-01-16" with Blood Pressure reading 145:92
    Given That patient visited for Hypertension on "2024-02-02" with Blood Pressure reading 140:87
    Given That patient visited for Hypertension on "2024-05-02" with Blood Pressure reading 136:84
    Given Update that patient with the following attributes
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
#    Given That patient visited for Blood Sugar on "2024-01-16" // TODO

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
    Given That patient visited for Hypertension on "2023-10-20" with Blood Pressure reading 147:89
    Given That patient visited for Hypertension on "2023-11-27" with Blood Pressure reading 142:87
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 140:83
    Given That patient visited for Hypertension on "2024-02-07" with Blood Pressure reading 135:84
    Given That patient visited for Hypertension on "2024-03-03" with Blood Pressure reading 134:83
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 147:87

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
    Given That patient visited for Hypertension on "2023-10-20" with Blood Pressure reading 142:95
    Given That patient visited for Hypertension on "2023-11-27" with Blood Pressure reading 139:90
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 132:82
    Given That patient visited for Hypertension on "2024-02-07" with Blood Pressure reading 128:78
    Given That patient visited for Hypertension on "2024-03-03" with Blood Pressure reading 140:88

    Given Export the analytics

    Given Run the Hypertension data aggregation

    Then The value of PI "HTN - Cumulative registrations" should be
      | 202407 | 7 |
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

  Scenario: Patients inactive for more than 12 months should be included
  #TEI LTFU
