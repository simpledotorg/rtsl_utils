Feature: Demo Dashboard Auditing


  Scenario: Check HTN - Cumulative registrations program indicator
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "YQj5qpbzQxh"

    #
    # Patient 1 - Dead patient
    #
    Given I create a new Patient on "2024-01-16" for this Facility with the following characteristics
      | sB1IHYu2xQT   | Test   |
      | YJGACwhN0St   | true   |
      | HTN diagnosis | YES    |
      | NI0QRzJvQ0k   | 32     |
      | Ot616hCy9j7   | KOLARA |
      | ENRjVGxVL6l   | TEST   |
      | v4DnYfXn9Mu   | YES    |
      | fI1P3Mg1zOZ   | ACTIVE |
    Given That patient visited for Hypertension on "2024-01-16" with Blood Pressure reading 145:92
    Given That patient visited for Hypertension on "2024-01-16" with Blood Pressure reading 145:92
    Given That patient visited for Hypertension on "2024-02-02" with Blood Pressure reading 140:87
    Given That patient visited for Hypertension on "2024-05-02" with Blood Pressure reading 136:84
#    Given That patient died on "2024-06-02" // TODO

    #
    # Patient 2 - Not a hypertension patient
    #
    Given I create a new Patient on "2024-01-16" for this Facility with the following characteristics
      | Given name    | Fabian |
      | Family name   | Moore  |
      | HTN diagnosis | No     |
      | v4DnYfXn9Mu   | YES    |
      | NI0QRzJvQ0k   | 32     |
      | Ot616hCy9j7   | KOLARA |
      | fI1P3Mg1zOZ   | ACTIVE |
      | YJGACwhN0St   | true   |
#    Given That patient visited for Blood Sugar on "2024-01-16" // TODO

    #
    # Patient 3
    #
    Given I create a new Patient on "2024-01-16" for this Facility with the following characteristics
      | Given name    | Sue     |
      | Family name   | Perkins |
      | HTN diagnosis | YES     |
      | v4DnYfXn9Mu   | YES     |
      | NI0QRzJvQ0k   | 32      |
      | Ot616hCy9j7   | KOLARA  |
      | fI1P3Mg1zOZ   | ACTIVE  |
      | YJGACwhN0St   | true    |
    Given That patient visited for Hypertension on "2023-10-20" with Blood Pressure reading 147:89
    Given That patient visited for Hypertension on "2023-11-27" with Blood Pressure reading 142:87
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 140:83
    Given That patient visited for Hypertension on "2024-02-07" with Blood Pressure reading 135:84
    Given That patient visited for Hypertension on "2024-03-03" with Blood Pressure reading 134:83
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 147:87

    #
    # Patient 4
    #
    Given I create a new Patient on "2024-01-16" for this Facility with the following characteristics
      | Given name    | Kiran  |
      | Family name   | Kishor |
      | HTN diagnosis | YES    |
      | v4DnYfXn9Mu   | YES    |
      | NI0QRzJvQ0k   | 32     |
      | Ot616hCy9j7   | KOLARA |
      | fI1P3Mg1zOZ   | ACTIVE |
      | YJGACwhN0St   | true   |
    Given That patient visited for Hypertension on "2023-10-20" with Blood Pressure reading 142:95
    Given That patient visited for Hypertension on "2023-11-27" with Blood Pressure reading 139:90
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 132:82
    Given That patient visited for Hypertension on "2024-02-07" with Blood Pressure reading 128:78
    Given That patient visited for Hypertension on "2024-03-03" with Blood Pressure reading 140:88

    Given Export the analytics

    Given Run the Hypertension data aggregation

    Then The value of PI "HTN - Cumulative registrations" should be
      | 202407 | 3 |
      | 202406 | 3 |
      | 202405 | 3 |
      | 202404 | 3 |
      | 202403 | 3 |
      | 202402 | 3 |
      | 202401 | 3 |
      | 202312 | 0 |

  Scenario: Load Test Patients and check results in a different org unit
    Given I create a new OrgUnit
    Given I assign the current user to the current orgUnit
    Given I register that Facility for program "YQj5qpbzQxh"


    #
    # Calculate aggregates
    #
    
# TODO


    #
    # Check data values
    #
#    Then I navigate to the DHIS2 homepage
#    Then I navigate to web dashboard "ONjdoWOUDp9"
#    Then I filter only on the Current Facility

