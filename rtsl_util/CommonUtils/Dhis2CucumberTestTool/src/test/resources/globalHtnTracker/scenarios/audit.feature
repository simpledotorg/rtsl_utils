@globalHypertensionTracker
Feature: Audit Global HTN Tracker dashboard

  Scenario: Load data to a dataset and test the indicators
      Given I create a new orgUnit at level 4
      And I sign in as a user with the user-role "Program Officer"
      And The user has access to the above orgUnit
    And The user fills out the dataset form:"HTN Global Tracker" for the period "thisQuarter" and for the orgUnit "Test Audit State 1" with following data:
      | Ry0CdyE2MbR | 20 |
      | d1xhcUY3Ltl | 30 |
      | XON6255Ko2l | 60 |
    When Export the analytics
    Then The value of "Indicator":"HVbaU9OrnTp" with period type "Quarter" should be
      | 4_QuartersAgo | 0    |
      | 3_QuartersAgo | 0    |
      | 2_QuartersAgo | 19.6 |
      | 1_QuarterAgo  | 1.3  |
      | thisQuarter   | 0    |
#"organisationUnits": [
#        {
#            "name": "Abuja ",
#            "children": [],
#            "id": "w7DevAohfjz",
#            "level": 4,
#            "ancestors": [
#                {
#                    "id": "MyYOwOMMXAa",
#                    "level": 1
#                },
#                {
#                    "id": "UfmjvKM0UgF",
#                    "level": 2
#                },
#                {
#                    "id": "ubfmszAGaom",
#                    "level": 3
#                }
#            ]
#        }
