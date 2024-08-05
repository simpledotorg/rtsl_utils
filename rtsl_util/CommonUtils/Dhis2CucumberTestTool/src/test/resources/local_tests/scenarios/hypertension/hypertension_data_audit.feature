Feature: Hypertension Data Audit

  Scenario: Load all patients and audit the correctness of indicators
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"
    #
    # Patient 1 - Dead
    #
    Given I create a new Patient on "2024-01-16" for this Facility with the following attributes
      | Given name         | Joe          |
      | Family name        | Bloggs       |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-16" with following data
      | Systole  | 145 |
      | Diastole | 92  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-02" with following data
      | Systole  | 140 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-02" with following data
      | Systole  | 136 |
      | Diastole | 84  |
    Given That patient was updated on "2024-01-16" with the following attributes
      | NCD Patient Status | DIED |
    #
    # Patient 2 - Not a hypertension patient
    #
    Given I create a new Patient on "2024-03-20" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-20" with following data
      | HTN - Type of diabetes measure? | HBA1C |
      | HTN - Blood sugar reading       | 9     |
    #
    #  Patient 3 - Patient under care, patient under care registered before the past 3 months and controlled
    #
    Given I create a new Patient on "2023-10-20" for this Facility with the following attributes
      | Given name         | Sue          |
      | Family name        | Perkins      |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-10-20" with following data
      | Systole  | 147 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-11-27" with following data
      | Systole  | 142 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-01-04" with following data
      | Systole  | 140 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-02-07" with following data
      | Systole  | 135 |
      | Diastole | 84  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-03-03" with following data
      | Systole  | 134 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2024-05-10" with following data
      | Systole  | 137 |
      | Diastole | 86  |
    #
    #  Patient 4 - Patient under care, patient under care registered before the past 3 months and controlled
    #
    Given I create a new Patient on "2023-01-10" for this Facility with the following attributes
      | Given name         | Kiran        |
      | Family name        | Kishor       |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 32           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-01-10" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-02-08" with following data
      | Systole  | 139 |
      | Diastole | 90  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-03-12" with following data
      | Systole  | 132 |
      | Diastole | 82  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-04-15" with following data
      | Systole  | 128 |
      | Diastole | 72  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2023-05-12" with following data
      | Systole  | 140 |
      | Diastole | 88  |
#  Patient 5
#  Name: Lilly Martin
#  Registration date: 02-Dec-2023
#  Has_hypertension: ‘YES’
#  Visits:
#  02-Dec-2023 - Uncontrolled - [Bp_systolic: 142, Bp_diastolic: 95]
#  03-Jan-2024 - Controlled - [Bp_systolic: 132, Bp_diastolic: 82]
#  04-Feb-2024 - Uncontrolled - [Bp_systolic: 146, Bp_diastolic: 93]
#  03-Mar-2024 - Uncontrolled - [Bp_systolic: 143, Bp_diastolic: 89]
#  14-May-2024 - Controlled - [Bp_systolic: 138, Bp_diastolic: 87]
#  12-Jun-2024 - Uncontrolled - [Bp_systolic: 140, Bp_diastolic: 91]
#  Care status: ‘active’

#  Patient 6
#  Name: Ajesh Choudhary
#  Registration date: 12-Aug-2023
#  Visits:
#  12-Aug-2023 - Uncontrolled -  [Bp_systolic: 146, Bp_diastolic: 93]
#  10-Sep-2023 - Uncontrolled -  [Bp_systolic: 143, Bp_diastolic: 93]
#  07-Oct-2023 - Controlled -  [Bp_systolic: 138, Bp_diastolic: 83]
#  03-Nov-2023 - Uncontrolled -  [Bp_systolic: 143, Bp_diastolic: 91]
#  23-Jan-2024 - Uncontrolled -  [Bp_systolic: 148, Bp_diastolic: 93]
#  18-Feb-2024 - Controlled -  [Bp_systolic: 136, Bp_diastolic: 82]
#  Care status: ‘active


#  Patient 7
#  Name: Bianca Santos
#  Registration date: 12-Apr-2024
#  Has_hypertension: ‘YES’
#  Visits:
#  12-Apr-2024 - Uncontrolled - [Bp_systolic: 141, Bp_diastolic: 94]
#  10-May-2024 - Uncontrolled - [Bp_systolic: 141, Bp_diastolic: 92]
#  4-Jun-2024 - Controlled - [Bp_systolic: 139, Bp_diastolic: 86]
#  Care status: ‘alive’

#  Patient 8
#  Description: 12-month lost to follow-up
#  Name: Joanne Holme
#  Registration date:  10-Apr-2023
#  Has_hypertension: ‘YES’
#  Visits:
#  10-Apr-2023 - Uncontrolled - [Bp_systolic: 152, Bp_diastolic: 98]
#  Care status: ‘active’

#  Patient 9
#  Name: Karlo Petersen
#  Registration date:  14-Feb-2024
#  Has_hypertension: ‘YES’
#  Visits:
#  14-Feb-2024 - Uncontrolled - [Bp_systolic: 152, Bp_diastolic: 98]
#  12-Mar-2024 - Uncontrolled - [Bp_systolic: 145, Bp_diastolic: 94]
#  16-Apr-2024 - Uncontrolled - [Bp_systolic: 133, Bp_diastolic: 91]
#  10-May-2024 - Controlled - [Bp_systolic: 128, Bp_diastolic: 76]
#  06-Jun-2024 - Controlled - [Bp_systolic: 125, Bp_diastolic: 77]
#  Care status: ‘active’

#  Patient 10
#  Name: Adrien Palomo
#  Registration date:  14-Dec-2023
#  Has_hypertension: ‘YES’
#  Visits:
#  14-Dec-2023 - Uncontrolled - [Bp_systolic: 152, Bp_diastolic: 98]
#  12-Jan-2024 - Uncontrolled - [Bp_systolic: 145, Bp_diastolic: 94]
#  8-Mar-2024 - Controlled - [Bp_systolic: 133, Bp_diastolic: 77]
#  Care status: ‘active’

#  Patient 11
#  Description: Patient that is ‘Visit with no BP’, registered as ‘no visit’ at the latest visit
#  Name: Iverna Terrazas
#  Registration date:  14-Nov-2023
#  Has_hypertension: ‘YES’
#  Visits:
#  14-Nov-2023 - Uncontrolled - [Bp_systolic: 152, Bp_diastolic: 98]
#  25-Dec-2023 - Uncontrolled - [Bp_systolic: 145, Bp_diastolic: 94]
#  7-Feb-2024 - Controlled - [Bp_systolic: 137, Bp_diastolic: 88]
#  10-Mar-2024 - Controlled - [Bp_systolic: 133, Bp_diastolic: 89]
#  6-May-2024 - Controlled - [Bp_systolic: 135, Bp_diastolic: 85]
#  1-Jun-2024 - Controlled - [Bp_systolic: 130, Bp_diastolic: 81]
#  8-Jun-2024 - Visit [no BP measure], Diabetes data only
#  8-July-2024 - Visit [no BP measure], Diabetes data only
#  Care status: ‘active’
