Feature: Audit the program indicator: HTN - Patients under care registered before the past 3 months

  Scenario: All patients must be hypertensive
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
      | HTN diagnosis      | NO           |
      | DM diagnosis       | YES          |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 206      |
      | HTN - Blood sugar unit          | MG_OR_DL |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 134      |
      | HTN - Blood sugar unit          | MG_OR_DL |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 130      |
      | HTN - Blood sugar unit          | MG_OR_DL |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-07-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 133      |
      | HTN - Blood sugar unit          | MG_OR_DL |

    #
    # Patient 2
    #
    Given I create a new Patient on "2024-01-01" for this Facility with the following attributes
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

    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-01" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-02-29" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-07-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    Given Export the analytics
    Given Run the Hypertension data aggregation
    # NOTE the PI name has a '.' at the end.
    Given The value of PI "HTN - Patients under care registered before the past 3 months." should be
      | 202407 | 1 |
      | 202406 | 1 |
      | 202405 | 1 |
      | 202404 | 1 |
      | 202403 | 0 |
      | 202402 | 0 |
      | 202401 | 0 |
      | 202312 | 0 |
      | 202311 | 0 |
      | 202310 | 0 |
      | 202309 | 0 |
      | 202308 | 0 |

  Scenario: All dead patients are excluded
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"
    #
    # Patient 1 -Dead
    #
    Given I create a new Patient on "2024-02-16" for this Facility with the following attributes
      | Given name         | Fabian       |
      | Family name        | Moore        |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-02-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 200      |
      | HTN - Blood sugar unit          | MG_OR_DL |


    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 130      |
      | HTN - Blood sugar unit          | MG_OR_DL |

    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 130      |
      | HTN - Blood sugar unit          | MG_OR_DL |

    Given That patient has a "Hypertension & Diabetes visit" event on "2024-07-16" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 130      |
      | HTN - Blood sugar unit          | MG_OR_DL |

    Given That patient was updated on "2024-07-16" with the following attributes
      | HTN - NCD Patient Status | DIED |

    #
    # Patient 2
    #
    Given I create a new Patient on "2024-01-01" for this Facility with the following attributes
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

    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-01" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-02-29" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-07-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    Given Export the analytics
    Given Run the Hypertension data aggregation
    # NOTE the PI name has a '.' at the end.
    Given The value of PI "HTN - Patients under care registered before the past 3 months." should be
      | 202407 | 1 |
      | 202406 | 1 |
      | 202405 | 1 |
      | 202404 | 1 |
      | 202403 | 0 |
      | 202402 | 0 |
      | 202401 | 0 |
      | 202312 | 0 |
      | 202311 | 0 |
      | 202310 | 0 |
      | 202309 | 0 |
      | 202308 | 0 |

  Scenario: All patients must be registered before 3 months
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"
    #
    # Patient 1
    #
    Given I create a new Patient on "2023-10-20" for this Facility with the following attributes
      | Given name         | Fabian       |
      | Family name        | Moore        |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | NO           |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-10-20" with following data
      | Systole  | 147 |
      | Diastole | 95  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-12-20" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-27" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-07-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    #
    # Patient 2
    #
    Given I create a new Patient on "2024-01-01" for this Facility with the following attributes
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

    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-01" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-02-29" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-07-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    Given Export the analytics
    Given Run the Hypertension data aggregation
    # NOTE the PI name has a '.' at the end.
    Given The value of PI "HTN - Patients under care registered before the past 3 months." should be
      | 202407 | 2 |
      | 202406 | 2 |
      | 202405 | 2 |
      | 202404 | 2 |
      | 202403 | 1 |
      | 202402 | 1 |
      | 202401 | 1 |
      | 202312 | 0 |
      | 202311 | 0 |
      | 202310 | 0 |
      | 202309 | 0 |
      | 202308 | 0 |

  Scenario: All patients who have their last visit before 12 months must be excluded
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"
    #
    # Patient 1
    #
    Given I create a new Patient on "2024-01-20" for this Facility with the following attributes
      | Given name         | Fabian       |
      | Family name        | Moore        |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | NO           |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |

    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-20" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-07-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    #
    # Patient 2
    #
    Given I create a new Patient on "2024-01-01" for this Facility with the following attributes
      | Given name         | Joey         |
      | Family name        | Joseph       |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | v4DnYfXn9Mu        | NO           |
      | NI0QRzJvQ0k        | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |

    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-01" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-02-29" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-07-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |

    #
    # Patient 3
    #
    Given I create a new Patient on "2023-02-01" for this Facility with the following attributes
      | Given name         | Fabian       |
      | Family name        | Moore        |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | NO           |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-02-01" with following data
      | Systole  | 147 |
      | Diastole | 89  |

    Given Export the analytics
    Given Run the Hypertension data aggregation

    # NOTE the PI name has a '.' at the end.
    Given The value of PI "HTN - Patients under care registered before the past 3 months." should be
      | 202407 | 2 |
      | 202406 | 2 |
      | 202405 | 2 |
      | 202404 | 2 |
      | 202403 | 0 |
      | 202402 | 0 |
      | 202401 | 1 |
      | 202312 | 1 |
      | 202311 | 1 |
      | 202310 | 1 |
      | 202309 | 1 |
      | 202308 | 1 |
