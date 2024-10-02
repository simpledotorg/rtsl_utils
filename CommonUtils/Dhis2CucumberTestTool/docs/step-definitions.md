# Step Definitions

## Organisation unit

All organisation units created in the test suite will be under a test root organisation unit called “TEST_COUNTRY”. When you create an organisation unit in any test scenario without specifying any level, the system will create one at level 5 with all the ancestor organisation units above it, i.e., levels 2 to 4.

#### Step Definitions

1. I create a new Facility
1. I create a new OrgUnit
1. I create a new organisationUnit at level `{int}`

<details><summary>Examples</summary>
```feature
Given I create a new OrgUnit
Given I create a new Facility
Given I create a new organisationUnit at level 4
Given I create a new organisationUnit at level 5
```
</details>


## Organisation unit Access

A user should be able to access the test organisation units created in the tests.
The default user in the test suite has “Superuser”  permissions. Currently, the tool only supports running tests at organisation unit level 5. In order to support running tests at multiple levels in the future, the user is given access to the test root organisation unit, “TEST_COUNTRY”.

#### Step Definitions

1. I assign the current user to the current orgUnit

```feature
Given I assign the current user to the current orgUnit
```

1. I have access to an organisation unit at level `{int}`
    * This step creates an organisation unit at specified level and the current user is given access to the the test root organisation unit.

```feature
Given I have access to an organisation unit at level 5
```

## User Authorization

This step definition is a placeholder to make it explicit that the user we are using in the test suite is a user with 'Superuser' access. This step definition can be further extended to support other user roles in future.

#### Step Definitions

I am signed in as a user with role `{string}`

where

- `{string}` is the User Role

```feature
Given I am signed in as a user with role "Superuser"
```

## Program

An organisation unit should be part of a program to enrol TEIs. The newly created organisation units must be given access to the program.

#### Step Definitions

1. I register that Facility for program `{string}`
1. I register that organisation unit for program `{string}`

where

- `{string}` is the name of the program

```feature
Given I register that Facility for program "Hypertension & Diabetes"
```

## Tracked Entity Instance (TEI)

### Creation

#### Step Definitions

1. I create a new TEI on `{string}` at this organisation unit with the following attributes `{Data Table}`
1. I create a new TEI on `{string}` for this OrgUnit with the following attributes `{Data Table}`
1. I create a new Patient on `{string}` at this organisation unit with the following attributes `{Data Table}`
1. I create a new TEI on `{string}` for this Facility with the following attributes `{Data Table}`

where

- `{string}` - Use relative date like '6_MonthsAgo'[^1]. See more about relative date at the bottom of the doc
- `{Data Table}` - A table in markdown format

<details><summary>Example: Create new TEI 7 months ago</summary>
```feature
Given I create a new TEI on "7_MonthsAgo" at this organisation unit with the following attributes
| GEN - Given name                     | Some         |
| GEN - Family name                    | Body         |
| GEN - Sex                            | MALE         |
| HTN - Does patient have hypertension?| YES          |
| HTN - Does patient have diabetes?    | YES          |
| GEN - Date of birth                  | 1999-09-09   |
| Address (current)                    | 1 New Street |
| District                             | Old Town     |
| HTN - Consent to record data         | true         |
| HTN - NCD Patient Status             | ACTIVE       |
```
</details>

### Updating

#### Step Definitions

1. That TEI was updated on `{string}` with the following attributes `{Data Table}`

where

- `{string}` - Use relative date like '6_MonthsAgo'. See more about relative date at the bottom of the doc
- `{Data Table}` - A table in markdown format

<details><summary>Example: Update new TEI 6 months ago</summary>
```feature
Given That TEI was updated on "6_MonthsAgo" with the following attributes
    | HTN - NCD Patient Status | DIED  |
    | GEN - Family name        | David |
```
</details>

## Events

### Creation

#### Step Definitions

1. That TEI has a `{string}` event on `{string}` with following data `{Data Table}`

where

- `{string}` - Name of the program stage/event
- `{string}` - Use relative date like '6_MonthsAgo'. See more about relative date at the bottom of the doc
- `{Data Table}` - A table in markdown format.

<details><summary>Example: Record an event 7 months ago</summary>
```feature
Given That TEI has a "Hypertension & Diabetes visit" event on "7_MonthsAgo" with following data
   | HTN - Type of diabetes measure? | FBS      |
   | HTN - Blood sugar reading       | 130      |
   | HTN - Blood sugar unit          | MG_OR_DL |
   | Systole                         | 142      |
   | Diastole                        | 95       |
```
</details>

### Schedule Event

#### Step Definitions

1. That TEI has a `{string}` event scheduled for `{string}`

where

- `{string}` - Name of the program stage/event
- `{string}` - Use relative date like '6_MonthsAgo'. See more about relative date at the bottom of the doc

```feature
Given That TEI has a "Hypertension & Diabetes visit" event scheduled for "6_MonthsAgo"
```

### Returned to care event after an appointment

#### Step Definitions

1. That TEI has a `{string}` event on `{string}` which was scheduled on `{string}` with following data

where

- `{string}` - Name of the program stage/event
- `{string}` - Execution date. Use relative date like '6_MonthsAgo'. See more about relative date at the bottom of the doc
- `{string}` - Appointment Date. Use relative date like '6_MonthsAgo'. See more about relative date at the bottom of the doc
- `{Data Table}` - A table in markdown format

<details><summary>Example: Record a scheduled event</summary>
```feature
Given That TEI has a "Hypertension & Diabetes visit" event on "3_MonthsAgo" which was scheduled on "5_MonthsAgo" with following data
    | Systole  | 142 |
    | Diastole | 95  |
```
</details>


## Analytics

### Wait Time

The overdue triggers take about 200ms to 900ms to run the logic before saving an event to the DB. So it's recommended to wait for 1 second after you have created all the events.

#### Step Definitions

1. I wait for `{int}` seconds
1. I wait for `{int}` second

### Export Analytics

This step is required to refresh the analytics tables before generating or querying any reports.

#### Step Definitions

1. I export the analytics
1. Export the analytics

```feature
When I export the analytics
```

## Data Aggregation

This step aggregates and saves the program indicator data to the corresponding data elements. This step can be skipped if you are testing a program indicator.

#### Step Definitions

1. I run the hypertension data aggregation
1. Run the Hypertension data aggregation

```feature
When I run the hypertension data aggregation
```

## Data Dimensions (dx)

This involves testing Program Indicators, Indicators and Data Elements

### Step Definitions

1. The value of `{string}`:`{string}` with period type `{string}` should be

where

- `{string}` - Possible values are 'Program Indicator', 'PI', 'Data Element', 'Indicator'
- `{string}` - Name of the data dimension you want to test
- `{string}` - Possible values are 'Months' and 'Quarters'. Use it depending on how the dx is aggregated.

<details><summary>Example: Get value of program indicator aggregated quarterly</summary>
```feature
Then The value of "PI":"HTN - Patients with no visit in this quarter" with period type "Quarters" should be
      | 4_QuartersAgo | 0 |
      | 3_QuartersAgo | 0 |
      | 2_QuartersAgo | 1 |
      | 1_QuarterAgo  | 1 |
      | thisQuarter   | 0 |
```
</details>

<details><summary>Example: Get value of program indicator aggregated monthly</summary>
```feature
Then The value of "PI":"HTN - Overdue patients" with period type "Months" should be
      | thisMonth    | 1 |
      | 1_MonthAgo   | 1 |
      | 2_MonthsAgo  | 1 |
      | 3_MonthsAgo  | 2 |
      | 4_MonthsAgo  | 2 |
      | 5_MonthsAgo  | 1 |
      | 6_MonthsAgo  | 1 |
      | 7_MonthsAgo  | 0 |
      | 8_MonthsAgo  | 0 |
      | 9_MonthsAgo  | 0 |
      | 10_MonthsAgo | 0 |
      | 11_MonthsAgo | 0 |
      | 12_MonthsAgo | 0 |
```
</details>

## Cache

This step definition clears any hibernate cache and reloads the apps. However, it should be used with caution, as it can interrupt other scenarios when running in parallel.

#### Step Definitions

Clears cache

```feature
When Clears cache
```

## Relative Dates

We use relative dates throughout the test suite instead of absolute dates.
Check out the syntax to write relative dates below:
`interger_[Month[s]Ago,Quarter[s]Ago,Plus,Minus]_[integer]_[Month[s],Day[s]]`

<details><summary>Possible options…</summary>
- `x_Day[s]Ago`
- `x_Month[s]Ago`
- `x_Quarter[s]Ago`
- `x_Plus_x_Month[s]`
- `x_Minus_x_Month[s]`
- `x_Plus_x_Day[s]`
- `x_Minus_x_Day[s]`
- `x_Month[s]Ago_Plus_x_Day[s]`
- `x_Month[s]Ago_Minus_x_Day[s]`
- `x_Month[s]Ago_Plus_x_Month[s]`
- `x_Month[s]Ago_Minus_x_Month[s]`
- `x_Quarter[s]Ago_Plus_x_Day[s]`
- `x_Quarter[s]Ago_Minus_x_Day[s]`
- `x_Quarter[s]Ago_Plus_x_Month[s]`
- `x_Quarter[s]Ago_Minus_x_Month[s]`
</details>

[^1]: We use relative time in our tests because aggregations in DHIS2 happen in relative time. If we fix the time in the tests, there would come a time in the future where the tests break, because it's drifted outside the window of the original test.

