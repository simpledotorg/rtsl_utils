Feature: Demo Dashboard Auditing


  Scenario: Load Test Patients and check results
    Given I create a new Facility

    #
    # Patient 1 - Dead patient
    #
    Given I create a new Patient for this Facility with the following characteristics
              | Field | Value |
              | Name  | Joe Bloggs  |
              | Registration_Date  | 2024-01-16  |
              | Has_hypertension | 'YES' |
    Given That patient visited for Hypertension on "2024-01-16" with Blood Pressure reading 145:92
    Given That patient visited for Hypertension on "2024-01-16" with Blood Pressure reading 145:92
    Given That patient visited for Hypertension on "2024-02-02" with Blood Pressure reading 140:87
    Given That patient visited for Hypertension on "2024-05-02" with Blood Pressure reading 136:84
    Given That patient died on "2024-06-02"

    #
    # Patient 2 - Not a hypertension patient
    #
    Given I create a new Patient for this Facility with the following characteristics
              | Field | Value |
              | Name  | Fabian Moore  |
              | Registration_Date  | 2024-03-02  |
              | Has_hypertension | 'No' |
    Given That patient visited for Blood Sugar on "2024-01-16"

    #
    # Patient 3
    #
    Given I create a new Patient for this Facility with the following characteristics
              | Field | Value |
              | Name  | Sue Perkins  |
              | Registration_Date  | 2023-10-20  |
              | Has_hypertension | 'YES' |
    Given That patient visited for Hypertension on "2023-10-20" with Blood Pressure reading 147:89
    Given That patient visited for Hypertension on "2023-11-27" with Blood Pressure reading 142:87
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 140:83
    Given That patient visited for Hypertension on "2024-02-07" with Blood Pressure reading 135:84
    Given That patient visited for Hypertension on "2024-03-03" with Blood Pressure reading 134:83
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 147:87

    #
    # Patient 3
    #
    Given I create a new Patient for this Facility with the following characteristics
              | Field | Value |
              | Name  | Sue Perkins  |
              | Registration_Date  | 2023-10-20  |
              | Has_hypertension | 'YES' |
    Given That patient visited for Hypertension on "2023-10-20" with Blood Pressure reading 147:89
    Given That patient visited for Hypertension on "2023-11-27" with Blood Pressure reading 142:87
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 140:83
    Given That patient visited for Hypertension on "2024-02-07" with Blood Pressure reading 135:84
    Given That patient visited for Hypertension on "2024-03-03" with Blood Pressure reading 134:83
    Given That patient visited for Hypertension on "2024-01-04" with Blood Pressure reading 147:87

    #
    # Calculate aggregates
    #
    
# TODO


    #
    # Check data values
    #
    Then I navigate to the DHIS2 homepage
    Then I navigate to web dashboard "ONjdoWOUDp9"
    Then I filter only on the Current Facility
    



