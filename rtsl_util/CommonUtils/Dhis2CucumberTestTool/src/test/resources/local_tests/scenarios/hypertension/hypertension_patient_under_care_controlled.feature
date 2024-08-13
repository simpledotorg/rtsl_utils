 Feature: Audit the PI - HTN PUC registered before 3 months and is controlled in the last 3 months
 Scenario: All patients should be hypertensive
  Given I create a new Patient on "7_MonthsAgo" for this Facility with the following attributes
   | name                           |   |
   | does patient have hypertension | YES |
  Given That patient has a "Hypertension & Diabetes visit" event on "7_monthsAgo" with following data
   | HTN - Type of diabetes measure? | FBS      |
   | HTN - Blood sugar reading       | 130      |
   | HTN - Blood sugar unit          | MG_OR_DL |

  Scenario: All patients should have a visit in the last 12 months
 Scenario: The last visit has to be controlled

