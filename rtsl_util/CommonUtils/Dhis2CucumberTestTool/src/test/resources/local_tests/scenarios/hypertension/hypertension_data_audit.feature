Feature: Hypertension Data Audit

  Scenario: Load all patients and audit the correctness of indicators
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "Hypertension & Diabetes"
    #
    # Patient 1 - Dead
    #
    Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 145 |
      | Diastole | 92  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" with following data
      | Systole  | 136 |
      | Diastole | 84  |
    Given That patient was updated on "7_MonthsAgo" with the following attributes
      | NCD Patient Status | DIED |
    #
    # Patient 2 - Not a hypertension patient
    #
    Given I create a new Patient on "5_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | HTN - Type of diabetes measure? | HBA1C |
      | HTN - Blood sugar reading       | 9     |
    #
    #  Patient 3 - Patient under care, patient under care registered before the past 3 months and controlled
    #
    Given I create a new Patient on "10_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" with following data
      | Systole  | 137 |
      | Diastole | 86  |
    #
    #  Patient 4 - Patient under care, patient under care registered before the past 3 months and controlled
    #
    Given I create a new Patient on "19_MonthsAgo" for this Facility with the following attributes
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
    Given That patient has a "Hypertension & Diabetes visit" event on "19_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    Given That patient has a "Hypertension & Diabetes visit" event on "18_MonthsAgo" with following data
      | Systole  | 139 |
      | Diastole | 90  |
    Given That patient has a "Hypertension & Diabetes visit" event on "17_MonthsAgo" with following data
      | Systole  | 132 |
      | Diastole | 82  |
    Given That patient has a "Hypertension & Diabetes visit" event on "16_MonthsAgo" with following data
      | Systole  | 128 |
      | Diastole | 72  |
    Given That patient has a "Hypertension & Diabetes visit" event on "15_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 88  |
    #
    #  Patient 5
    #
    Given I create a new Patient on "8_MonthsAgo" for this Facility with the following attributes
      | Given name         | Lilly        |
      | Family name        | Martin       |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 42           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "8_MonthsAgo" with following data
      | Systole  | 142 |
      | Diastole | 95  |
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 132 |
      | Diastole | 82  |
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 146 |
      | Diastole | 93  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 143 |
      | Diastole | 89  |
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" with following data
      | Systole  | 138 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" with following data
      | Systole  | 140 |
      | Diastole | 91  |
  #
  #  Patient 6
  #
    Given I create a new Patient on "12_MonthsAgo" for this Facility with the following attributes
      | Given name         | Ajesh        |
      | Family name        | Choudhary    |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 42           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "12_MonthsAgo" with following data
      | Systole  | 146 |
      | Diastole | 93  |
    Given That patient has a "Hypertension & Diabetes visit" event on "11_MonthsAgo" with following data
      | Systole  | 143 |
      | Diastole | 93  |
    Given That patient has a "Hypertension & Diabetes visit" event on "10_MonthsAgo" with following data
      | Systole  | 138 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "9_MonthsAgo" with following data
      | Systole  | 143 |
      | Diastole | 91  |
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 148 |
      | Diastole | 93  |
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 136 |
      | Diastole | 82  |
  #
  #  Patient 7
  #
    Given I create a new Patient on "4_MonthsAgo" for this Facility with the following attributes
      | Given name         | Bianca       |
      | Family name        | Santos       |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 42           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "4_MonthsAgo" with following data
      | Systole  | 141 |
      | Diastole | 94  |
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" with following data
      | Systole  | 143 |
      | Diastole | 93  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2_MonthsAgo" with following data
      | Systole  | 138 |
      | Diastole | 83  |
  #
  #  Patient 8 - Patient has not visited for more than 12 months
  #
    Given I create a new Patient on "16_MonthsAgo" for this Facility with the following attributes
      | Given name         | Joanne       |
      | Family name        | Holme        |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 42           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "16_MonthsAgo" with following data
      | Systole  | 152 |
      | Diastole | 98  |
  #
  #  Patient 9
  #
    Given I create a new Patient on "6_MonthsAgo" for this Facility with the following attributes
      | Given name         | Karlo        |
      | Family name        | Petersen     |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 42           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 152 |
      | Diastole | 98  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 145 |
      | Diastole | 98  |
    Given That patient has a "Hypertension & Diabetes visit" event on "4_MonthsAgo" with following data
      | Systole  | 138 |
      | Diastole | 91  |
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" with following data
      | Systole  | 128 |
      | Diastole | 76  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2_MonthsAgo" with following data
      | Systole  | 125 |
      | Diastole | 77  |
  #
  #  Patient 10
  #
    Given I create a new Patient on "8_MonthsAgo" for this Facility with the following attributes
      | Given name         | Adrien       |
      | Family name        | Palomo       |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 42           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "8_MonthsAgo" with following data
      | Systole  | 152 |
      | Diastole | 98  |
    Given That patient has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
      | Systole  | 143 |
      | Diastole | 93  |
    Given That patient has a "Hypertension & Diabetes visit" event on "4_MonthsAgo" with following data
      | Systole  | 138 |
      | Diastole | 83  |
  #
  #  Patient 11 -  Patient that is ‘Visit with no BP’, registered as ‘no visit’ at the latest visit
  #
    Given I create a new Patient on "9_MonthsAgo" for this Facility with the following attributes
      | Given name         | Iverna       |
      | Family name        | Terrazas     |
      | Sex                | MALE         |
      | HTN diagnosis      | YES          |
      | DM diagnosis       | YES          |
      | Date of birth      | 42           |
      | Address            | Rose Gardens |
      | District           | KOLARA       |
      | Data consent       | true         |
      | NCD Patient Status | ACTIVE       |
    Given That patient has a "Hypertension & Diabetes visit" event on "9_MonthsAgo" with following data
      | Systole  | 146 |
      | Diastole | 93  |
    Given That patient has a "Hypertension & Diabetes visit" event on "8_MonthsAgo" with following data
      | Systole  | 143 |
      | Diastole | 93  |
    Given That patient has a "Hypertension & Diabetes visit" event on "6_MonthsAgo" with following data
      | Systole  | 138 |
      | Diastole | 83  |
    Given That patient has a "Hypertension & Diabetes visit" event on "5_MonthsAgo" with following data
      | Systole  | 137 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" with following data
      | Systole  | 135 |
      | Diastole | 87  |
    Given That patient has a "Hypertension & Diabetes visit" event on "2_MonthsAgo" with following data
      | Systole                         | 130      |
      | Diastole                        | 81       |
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 200      |
      | HTN - Blood sugar unit          | MG_OR_DL |
    Given That patient has a "Hypertension & Diabetes visit" event on "2_MonthsAgo_Plus_4_Days" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 130      |
      | HTN - Blood sugar unit          | MG_OR_DL |
    Given That patient has a "Hypertension & Diabetes visit" event on "1_MonthsAgo" with following data
      | HTN - Type of diabetes measure? | FBS      |
      | HTN - Blood sugar reading       | 100      |
      | HTN - Blood sugar unit          | MG_OR_DL |
    When Export the analytics
#    When Run the Hypertension data aggregation

    Then The value of "PI":"HTN - Cumulative dead patients" with period type "Months" should be
      | 12_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 10_MonthsAgo | 0 |
      | 9_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 7_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 1 |
      | 5_MonthsAgo  | 1 |
      | 4_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 1 |
      | 2_MonthsAgo  | 1 |
      | 1_MonthAgo   | 1 |
      | thisMonth    | 1 |
    Then The value of "PI":"HTN - Cumulative registrations" with period type "Months" should be
      | 12_MonthsAgo | 3 |
      | 11_MonthsAgo | 3 |
      | 10_MonthsAgo | 4 |
      | 9_MonthsAgo  | 5 |
      | 8_MonthsAgo  | 7 |
      | 7_MonthsAgo  | 7 |
      | 6_MonthsAgo  | 8 |
      | 5_MonthsAgo  | 8 |
      | 4_MonthsAgo  | 9 |
      | 3_MonthsAgo  | 9 |
      | 2_MonthsAgo  | 9 |
      | 1_MonthAgo   | 9 |
      | thisMonth    | 9 |
    Then The value of "PI":"HTN - Monthly registrations" with period type "Months" should be
      | 12_MonthsAgo | 1 |
      | 11_MonthsAgo | 0 |
      | 10_MonthsAgo | 1 |
      | 9_MonthsAgo  | 1 |
      | 8_MonthsAgo  | 2 |
      | 7_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 1 |
      | 5_MonthsAgo  | 0 |
      | 4_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 0 |
      | 2_MonthsAgo  | 0 |
      | 1_MonthAgo   | 0 |
      | thisMonth    | 0 |
    Then The value of "PI":"HTN - Patients under care registered before the past 3 months." with period type "Months" should be
      | 12_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 10_MonthsAgo | 0 |
      | 9_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 7_MonthsAgo  | 2 |
      | 6_MonthsAgo  | 3 |
      | 5_MonthsAgo  | 5 |
      | 4_MonthsAgo  | 5 |
      | 3_MonthsAgo  | 6 |
      | 2_MonthsAgo  | 6 |
      | 1_MonthAgo   | 6 |
      | thisMonth    | 7 |
    Then The value of "PI":"HTN - Patients under care with controlled BP at latest visit in past 3 months and registered before the past 3 months" with period type "Months" should be
      | 12_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 10_MonthsAgo | 0 |
      | 9_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 7_MonthsAgo  | 0 |
      | 6_MonthsAgo  | 3 |
      | 5_MonthsAgo  | 3 |
      | 4_MonthsAgo  | 4 |
      | 3_MonthsAgo  | 4 |
      | 2_MonthsAgo  | 5 |
      | 1_MonthAgo   | 5 |
      | thisMonth    | 2 |
    Then The value of "PI":"HTN - Patients under care with no visit in past 3 months registered before the past 3 months" with period type "Months" should be
      | 12_MonthsAgo | 2 |
      | 11_MonthsAgo | 2 |
      | 10_MonthsAgo | 2 |
      | 9_MonthsAgo  | 2 |
      | 8_MonthsAgo  | 2 |
      | 7_MonthsAgo  | 2 |
      | 6_MonthsAgo  | 2 |
      | 5_MonthsAgo  | 2 |
      | 4_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 1 |
      | 2_MonthsAgo  | 2 |
      | 1_MonthAgo   | 2 |
      | thisMonth    | 2 |
    Then The value of "PI":"HTN - Patients under care with uncontrolled BP at latest visit in past 3 months registered before the past 3 months" with period type "Months" should be
      | 12_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 10_MonthsAgo | 0 |
      | 9_MonthsAgo  | 1 |
      | 8_MonthsAgo  | 1 |
      | 7_MonthsAgo  | 2 |
      | 6_MonthsAgo  | 0 |
      | 5_MonthsAgo  | 1 |
      | 4_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 0 |
      | 2_MonthsAgo  | 1 |
      | 1_MonthAgo   | 1 |
      | thisMonth    | 1 |
    Then The value of "PI":"HTN - Patients with controlled BP at latest visit in this quarter" with period type "Quarters" should be
      | 12_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 10_MonthsAgo | 0 |
      | 9_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 7_MonthsAgo  | 0 |
      | 6_MonthsAgo  | 3 |
      | 5_MonthsAgo  | 4 |
      | 4_MonthsAgo  | 4 |
      | 3_MonthsAgo  | 5 |
      | 2_MonthsAgo  | 3 |
      | 1_MonthAgo   | 3 |
      | thisMonth    | 3 |
    Then The value of "PI":"HTN - Patients with no visit in last 12 months" with period type "Months" should be
      | 12_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 10_MonthsAgo | 0 |
      | 9_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 7_MonthsAgo  | 0 |
      | 6_MonthsAgo  | 0 |
      | 5_MonthsAgo  | 0 |
      | 4_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 2 |
      | 2_MonthsAgo  | 2 |
      | 1_MonthAgo   | 2 |
      | thisMonth    | 2 |
    Then The value of "PI":"HTN - Patients with no visit in this quarter" with period type "Quarters" should be
      | 4_QuartersAgo | 1 |
      | 3_QuartersAgo | 1 |
      | 2_QuartersAgo | 0 |
      | 1_QuarterAgo  | 0 |
      | thisQuarter   | 1 |
    Then The value of "PI":"HTN - Patients with uncontrolled BP from latest visit in this quarter" with period type "Quarters" should be
      | 4_QuartersAgo | 0 |
      | 3_QuartersAgo | 0 |
      | 2_QuartersAgo | 2 |
      | 1_QuarterAgo  | 0 |
      | thisQuarter   | 0 |
    Then The value of "PI":"HTN - Patients with controlled BP at latest visit in this quarter" with period type "Quarters" should be
      | 4_QuartersAgo | 0 |
      | 3_QuartersAgo | 0 |
      | 2_QuartersAgo | 3 |
      | 1_QuarterAgo  | 1 |
      | thisQuarter   | 0 |
    Then The value of "PI":"HTN - Total patient registrations in previous quarter" with period type "Quarters" should be
      | 4_QuartersAgo | 1 |
      | 3_QuartersAgo | 1 |
      | 2_QuartersAgo | 4 |
      | 1_QuarterAgo  | 1 |
      | thisQuarter   | 1 |
