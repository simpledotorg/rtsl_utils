package org.rtsl.dhis2.cucumber.definitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.rtsl.dhis2.cucumber.utils.Helper.toISODateTimeString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.api.model.v40_2_2.AttributeInfo;
import org.rtsl.dhis2.cucumber.utils.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.utils.Dhis2IdConverter;
import org.rtsl.dhis2.cucumber.factories.OrganisationUnit;
import org.rtsl.dhis2.cucumber.factories.TrackedEntityInstance;
import org.rtsl.dhis2.cucumber.utils.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import jakarta.inject.Inject;
import jakarta.inject.Named;

public class Dhis2StepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitions.class);

    @Inject
    @Named("testClient")
    private Dhis2HttpClient dhis2HttpClient;

    @Inject
    @Named("testIdConverter")
    private Dhis2IdConverter testIdConverter;

    @Inject
    @Named("organisationUnit")
    private OrganisationUnit organisationUnit;

    @Inject
    @Named("trackedEntityInstance")
    private TrackedEntityInstance trackedEntityInstance;

    String currentOrgUnitId = null;
    String currentEnrollmentId = null;
    String currentTeiId = null;
    String currentEventId = null;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Get current org unit id.
     *
     * <p>
     * This is a helper method for FreeMarker templates.
     * </p>
     */
    public String getCurrentOrgUnitId() {
        return currentOrgUnitId;
    }

    /**
     * Get current event id.
     *
     * <p>
     * This is a helper method for FreeMarker templates.
     * </p>
     */
    public String getCurrentEventId() {
        return currentEventId;
    }

    /**
     * Get current enrollment id.
     *
     * <p>
     * This is a helper method for FreeMarker templates.
     * </p>
     */
    public String getCurrentEnrollmentId() {
        return currentEnrollmentId;
    }

    /**
     * Get current TEI id.
     *
     * <p>
     * This is a helper method for FreeMarker templates.
     * </p>
     */
    public String getCurrentTeiId() {
        return currentTeiId;
    }

    private Scenario scenario;

    @Before
    public void setup(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Create a new organisation unit.
     *
     * All tests (read "scenarios") will be run in an organisation unit (a.k.a
     * "org unit"). This is the org unit under which the analytics would be
     * aggregated. All org units would be created under a "TEST_COUNTRY". This
     * way, the data from the tests do not pollute existing data in the DHIS2
     * instance. When you create an organisation unit in any test scenario
     * without specifying any level, the system will create one at level 5 with
     * all the ancestor organisation units above it, i.e., levels 2 to 4.
     *
     * {@link OrganisationUnit}
     */
    @Given("I create a new Facility")
    @Given("I create a new OrgUnit")
    public void iCreateANewOrganisationUnitAtLevel() throws Exception {
        iCreateANewOrganisationUnitAtLevel(5);
    }

    /**
     * Create a new organisation unit at a specific level.
     *
     * @see #iCreateANewOrganisationUnitAtLevel()
     *
     * @param level The level to create the oganisation unit at.
     */
    @Given("I create a new organisationUnit at level {int}")
    public void iCreateANewOrganisationUnitAtLevel(int level) throws Exception {
        Map<String, String> newOrganisationUnit = organisationUnit.createOrganisationUnit(level);
        this.currentOrgUnitId = newOrganisationUnit.get("organisationUnitId");
        LOGGER.info("created OrgUnit: <{}> <{}>", currentOrgUnitId, newOrganisationUnit.get("organisationUnitName"));
        scenario.log("Created new OrgUnit with Id:" + currentOrgUnitId + " and Name:" + newOrganisationUnit.get("organisationUnitName"));
    }

    /**
     * Create an organisation unit at level if not exists, and grant access to existing user.
     *
     * A user should be able to access the test organisation units created in
     * the tests. The default user in the test suite has “Superuser”
     * permissions. Currently, the tool only supports running tests at
     * organisation unit level 5. In order to support running tests at multiple
     * levels in the future, the user is given access to the test root
     * organisation unit, “TEST_COUNTRY”.
     *
     * @param level The level of oganisation unit to grant access at.
     */
    @And("I have access to an organisation unit at level {int}")
    public void iHaveAccessToAnOrganisationUnitAtLevel(int level) throws Exception {
        iCreateANewOrganisationUnitAtLevel(level);
        iAssignTheCurrentUserToTheCurrentOrgUnit();
    }

    /**
     * Grant access to existing user to organisation unit at level.
     *
     * @see #iHaveAccessToAnOrganisationUnitAtLevel(int level)
     *
     * The difference between this method and the method above is that this
     * method does not create a new organisation unit. It only assigns the
     * current user permissions to the current organisation unit.
     */
    @Given("I assign the current user to the current orgUnit")
    public void iAssignTheCurrentUserToTheCurrentOrgUnit() throws Exception {
        String currentUserId = dhis2HttpClient.getCurrentUserId();
        String baseUserJson = dhis2HttpClient.doGet("api/users/" + currentUserId + "?fields=id,name,organisationUnits,userGroups,userRoles,dataViewOrganisationUnits,teiSearchOrganisationUnits");
        JsonNode rootNode = MAPPER.readTree(baseUserJson);
        ArrayNode organisationUnits = (ArrayNode) rootNode.get("organisationUnits");

        ObjectNode testRootId = MAPPER.createObjectNode().put("id", OrganisationUnit.getLevel1Id());
        organisationUnits.add(testRootId);

        ArrayNode dataViewOrganisationUnits = (ArrayNode) rootNode.get("dataViewOrganisationUnits");
        dataViewOrganisationUnits.add(testRootId);

        ArrayNode teiSearchOrganisationUnits = (ArrayNode) rootNode.get("teiSearchOrganisationUnits");
        teiSearchOrganisationUnits.add(testRootId);

        String modifiedJson = MAPPER.writeValueAsString(rootNode);
        dhis2HttpClient.doPutWithBody("api/users/" + currentUserId + "?mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE", modifiedJson);
        scenario.log("Current user: " + currentUserId + " has given access to the facility:" + currentOrgUnitId);
    }

    /**
     * Register an organizational unit in a program.
     *
     * <p>
     * A program in DHIS2 is a collection of operations (a.k.a "program
     * stages"). All organisation units should be part of a program. This
     * method registers an organisation unit into an existing program.
     * </p>
     *
     * <pre>
     * Given I register that Facility for program "Hypertension &amp; Diabetes"
     * </pre>
     *
     * @param program The name of the already existing program in the instance.
     */
    @Given("I register that Facility for program {string}")
    @Given("I register that organisation unit for program {string}")
    public void iRegisterThatOrganisationUnit(String program) throws Exception {
        String programName = testIdConverter.getProgramId(program);
        String baseProgramJson = dhis2HttpClient.doGet("api/programs/" + programName);
        JsonNode rootNode = MAPPER.readTree(baseProgramJson);
        ArrayNode organisationUnits = (ArrayNode) rootNode.get("organisationUnits");
        ObjectNode facilityId = MAPPER.createObjectNode();
        facilityId.put("id", currentOrgUnitId);
        organisationUnits.add(facilityId);
        String modifiedJson = MAPPER.writeValueAsString(rootNode);
        String response = dhis2HttpClient.doPutWithBody(
                "api/programs/" + programName + "?mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                modifiedJson);
        LOGGER.info("Response {}", response);
        scenario.log("Current facility: " + facilityId.get("id") + " has been assigned to the program:" + programName);
    }

    /**
     * Create a new TEI on a specific date.
     *
     * <p>
     * This creates a new tracked entity instance (TEI) in the instance. Since
     * report aggregations are done in relative terms — <code>2 months
     * ago</code>, <code>last quarter</code> — the dates we specify when
     * creating resources in the instance are all in relative terms. This makes
     * a blocking API call to the instance.
     * </p>
     *
     * <pre>
     * Given I create a new TEI on "7_MonthsAgo" at this organisation unit with the following attributes
     * | GEN - Given name                     | John         |
     * | GEN - Family name                    | Doe          |
     * | GEN - Sex                            | MALE         |
     * | HTN - Does patient have hypertension?| YES          |
     * | HTN - Does patient have diabetes?    | YES          |
     * | GEN - Date of birth                  | 1999-05-09   |
     * | Address (current)                    | Example Ave. |
     * | District                             | Some Town    |
     * | HTN - Consent to record data         | true         |
     * | HTN - NCD Patient Status             | ACTIVE       |
     * </pre>
     *
     * @param relativeEventDate The date to create the TEI, in relative terms.
     * @param dataTable The TEI attributes to create the TEI with.
     *
     * @throws Exception as an effect of the network call to the instance
     */
    @Given("I create a new TEI for this OrgUnit with the following attributes")
    @Given("I create a new TEI on {string} for this Facility with the following attributes")
    @Given("I create a new TEI on {string} at this organisation unit with the following attributes")
    @And("I create a new Patient on {string} at this organisation unit with the following attributes")
    public void iCreateANewPatientOnAtOrganisationUnitWithTheFollowingAttributes(String relativeEventDate, Map<String, String> dataTable) throws Exception {
        String eventDate = Period.toDateString(relativeEventDate);
        Map<String, String> newTei = trackedEntityInstance.create(dataTable, currentOrgUnitId, eventDate);
        this.currentTeiId = newTei.get("id");
        this.currentEnrollmentId = newTei.get("enrollmentId");
        scenario.log("Created new TEI with Id:" + currentTeiId + " and Enrollment with Id:" + currentEnrollmentId);
    }

    /**
     * Create an event for the TEI at the specified date.
     *
     * <p>
     * This creates an event that's happening at the time the event is scheduled.
     * </p>
     *
     * <pre>
     * And That TEI has a "Hypertension &amp; Diabetes visit" event on "7_MonthsAgo" with following data
     *   | Systole  | 145 |
     *   | Diastole | 92  |
     * </pre>
     *
     * @param relativeEventDate The date to create the TEI, in relative terms.
     * @param dataTable The TEI attributes to create the TEI with.
     *
     * @throws Exception as an effect of the network call to the instance
     *
     * @see #thatTeiHasAEventOnWhichWasScheduledOnWithFollowingData(String eventName, String relativeEventDate, String relativeScheduledDate, Map<String, String> dataTable)
     */
    @Given("That TEI has a {string} event on {string} with following data")
    public void thatTeiHasAEventOnWithFollowingData(String eventName, String relativeEventDate, Map<String, String> dataTable) throws Exception {
        thatTeiHasAEventOnWhichWasScheduledOnWithFollowingData(eventName, relativeEventDate, relativeEventDate, dataTable);
    }

    /**
     * Schedule an event for the TEI at the specified date.
     *
     * <p>
     * An event, in DHIS2, is an occurence of a program stage. This allows the create date (i.e. the date the event happens) and the scheduled date (i.e. the date the event was supposed to happen) to vary.
     * </p>
     *
     * <pre>
     * And That TEI has a "Hypertension &amp; Diabetes visit" event on "3_MonthsAgo" which was scheduled on "5_MonthsAgo" with following data
     *   | Systole  | 142 |
     *   | Diastole | 95  |
     * </pre>
     *
     * @param relativeEventDate when the event happened.
     * @param relativeScheduledDate when the event was scheduled for.
     * @param dataTable the attributes to create the event with.
     *
     * @throws Exception as an effect of the network call to the instance
     */
    @Given("That TEI has a {string} event on {string} which was scheduled on {string} with following data")
    public void thatTeiHasAEventOnWhichWasScheduledOnWithFollowingData(String eventName, String relativeEventDate, String relativeScheduledDate, Map<String, String> dataTable) throws Exception {
        String eventDateString = Period.toDateString(relativeEventDate);
        String scheduledDateString = Period.toDateString(relativeScheduledDate);
        Map<String, String> convertedDataTable = new HashMap<>();
        for (String dataElement : dataTable.keySet()) {
            String dataElementId;
            if (dataElement.equals("HTN - Blood sugar reading")) {
                String unitValue = dataTable.get("HTN - Blood sugar unit");
                String bloodSugarTypeValue = dataTable.get("HTN - Type of diabetes measure?");
                String bloodSugarType = getOptionNameFromCode(bloodSugarTypeValue);
                if (unitValue == null) {
                    dataElementId = testIdConverter.getDataElementId("HTN - Blood sugar reading: " + bloodSugarType);
                } else {
                    String unitName = getOptionNameFromCode(unitValue);
                    dataElementId = testIdConverter.getDataElementId("HTN - Blood sugar reading: " + bloodSugarTypeValue + " (" + unitName + ")");
                }
            } else {
                dataElementId = testIdConverter.getDataElementId(dataElement);
            }
            convertedDataTable.put(dataElementId, dataTable.get(dataElement));
        }
        Map<String, Object> templateContext = Map.of("data", this,
                "dataTable", convertedDataTable,
                "occurredAt", toISODateTimeString(eventDateString),
                "scheduledAt", toISODateTimeString(scheduledDateString),
                "programStageId", testIdConverter.getProgramStageId(eventName),
                "eventStatus", "COMPLETED"
        );
        createEvent(templateContext);
    }

    /**
     * Run and export the analytics.
     *
     * <pre>
     * When I export the analytics
     * </pre>
     *
     * <p>
     * <b>Caution!!!</b> This step is costly.
     * </p>
     *
     * <p>
     * In order to verify the impact of our setup steps on the state of DHIS2,
     * analytics need to be exported. This puts data in the necessary program
     * attributes as defined in the program. This step is compute heavy, so
     * it's recommended to have this after {@link waitUntilDBTriggerCompletion}.
     * </p>
     *
     * @throws Exception as an effect of the network call to the instance
     */
    @Given("Export the analytics")
    @Given("I export the analytics")
    public void exportTheAnalytics() throws Exception {
        String exportAnalyticsJobId = testIdConverter.getJobConfigurationId("Matview Refresh");
        String jobStatus;
        String lastRuntimeExecution;
        String response;
        String lastExecutedStatus;

        executeJob(exportAnalyticsJobId);
        do {
            // Loop until job is finished
            response = dhis2HttpClient.doGet("api/jobConfigurations/" + exportAnalyticsJobId);
            JsonNode jobConfigurations = MAPPER.readTree(response);
            jobStatus = jobConfigurations.get("jobStatus").asText();
            lastRuntimeExecution = jobConfigurations.get("lastRuntimeExecution").asText();
            lastExecutedStatus = jobConfigurations.get("lastExecutedStatus").asText();
        } while (jobStatus.equals("RUNNING"));
        LOGGER.info("Response {}", response);
        if (lastExecutedStatus.equals("STOPPED"))
            throw new Exception("Analytics job with Id:" + exportAnalyticsJobId + " stopped. Please check the job configurations");

        scenario.log("Analytics job with Id:" + exportAnalyticsJobId + " took " + lastRuntimeExecution + " time to complete.");
    }

    /**
     * Run the HTN data aggregation.
     *
     * <p>
     * This runs the specific "ADEX - Hypertension dashboard" aggregation. In
     * subsequent versions, this would be a general step to run any aggregation
     * configured on the instance.
     * </p>
     *
     * @throws Exception as an effect of the network call to the instance
     */
    @Given("Run the Hypertension data aggregation")
    @Given("I run the hypertension data aggregation")
    public void runTheHypertensionDataAggregation() throws Exception {
        // TODO: Generalize this to run any aggregation on the instance.
        String dataAggregationJobId = testIdConverter.getJobConfigurationId("ADEX - Hypertension dashboard");
        String jobStatus;
        String lastRuntimeExecution;
        String lastExecutedStatus;
        String response;

        executeJob(dataAggregationJobId);
        do {
            // Loop until job is finished
            response = dhis2HttpClient.doGet("api/jobConfigurations/" + dataAggregationJobId);
            JsonNode jobConfigurations = MAPPER.readTree(response);
            jobStatus = jobConfigurations.get("jobStatus").asText();
            lastRuntimeExecution = jobConfigurations.get("lastRuntimeExecution").asText();
            lastExecutedStatus = jobConfigurations.get("lastExecutedStatus").asText();

        } while (jobStatus.equals("RUNNING"));
        LOGGER.info("Response {}", response);
        if (lastExecutedStatus.equals("STOPPED"))
            throw new Exception("Data exchange and aggregation job with Id:" + dataAggregationJobId + " stopped. Please check the job configurations");
        scenario.log("Data exchange and aggregation job with Id:" + dataAggregationJobId + " took " + lastRuntimeExecution + " time to complete.");

    }

    /**
     * Verify some data over a period in time.
     *
     * <pre>
     * Then The value of "PI":"HTN - Overdue patients" with period type "Months" should be
     *   | thisMonth    | 1 |
     *   | 5_MonthAgo   | 1 |
     *   | 9_MonthsAgo  | 1 |
     * </pre>
     *
     * This step allows you define the data you expect to see in a specific
     * dimension in your DHIS2 instance. The {@code dimensionItemType} can be
     * specified either in long form ("Program Indicator") or in short form
     * ("PI"). The {@code dimensionItemName} must be specified in full as is
     * defined in the DHIS2 instance.
     *
     * @param dimensionItemType the dimension type to check
     * @param dimensionItemName the dimension to check
     * @param periodType the categorization of time this validation is concerned with
     * @param dataTable an expectation map of time-window and expectation
     *
     * @throws Exception as an effect of the network call to the instance
     */
    @Then("The value of {string}:{string} with period type {string} should be")
    public void theValueOfShouldBe(String dimensionItemType, String dimensionItemName, String periodType, Map<String, String> dataTable) throws Exception {
        String dimensionItemId = getDimensionItemId(dimensionItemType, dimensionItemName);
        Map<String, String> actualPeriodValues = getAnalyticData(dimensionItemId, dimensionItemName, periodType);
        for (String relativePeriod : dataTable.keySet()) {
            String period = Period.toReportingDateString(relativePeriod, periodType);
            assertEquals(dataTable.get(relativePeriod),
                    actualPeriodValues.get(period),
                    dimensionItemName + ": <" + dimensionItemId + "> for the <" + period + "(" + relativePeriod + ")" + "> in Organisation Unit:<" + this.currentOrgUnitId + ">." +
                            "\nNote: Ensure you have aggregated the data after exporting the analytics.\n");
        }
    }

    private String getDimensionItemId(String dimensionItemType, String dimensionItemName) {
        switch (dimensionItemType) {
            case "Program Indicator", "PI" -> {
                return testIdConverter.getProgramIndicatorId(dimensionItemName);
            }
            case "Indicator" -> {
                return testIdConverter.getIndicatorId(dimensionItemName);
            }
            case "Data Element" -> {
                return testIdConverter.getDataElementId(dimensionItemName);
            }
            default -> throw new IllegalStateException("Unexpected value: " + dimensionItemType);
        }
    }

    /**
     * Update the TEA at a specific date.
     *
     * <pre>
     * And That TEI was updated on "2_MonthsAgo" with the following attributes
     *   | HTN - NCD Patient Status | DIED |
     * </pre>
     *
     * This sets updates the tracked entity attributes with the data specified in the table below.
     *
     * @param relativeDate the time, in relative terms, to update the tracked entity instance.
     * @param dataTable the attributes to update the tracked entity instance with.
     *
     * @throws Exception as an effect of the network call to the instance
     */
    @Given("That TEI was updated on {string} with the following attributes")
    public void thatTeiWasUpdatedOnWithTheFollowingAttributes(String relativeDate, Map<String, String> dataTable) throws Exception {
        String visitDate = Period.toDateString(relativeDate);
        trackedEntityInstance.update(dataTable, currentOrgUnitId, this.currentTeiId, visitDate);
        scenario.log("Created new TEI with Id:" + currentTeiId + " updated");
    }

    /**
     * Create an event scheduled at a specific date.
     *
     * <pre>
     * And That TEI has a "Hypertension &amp; Diabetes visit" event scheduled for "6_MonthsAgo_Minus_1_Day"
     * </pre>
     *
     * This creates an event for the tracked entity instance at a scheduled
     * time which hasn't occured yet. This step often follows {@link
     * #iCreateANewPatientOnAtOrganisationUnitWithTheFollowingAttributes}, and
     * references the tracked entity instance created in that step.
     *
     * @param eventName name of the event to create
     * @param relativeEventDate expected schedule time of the event
     *
     * @throws Exception as an effect of the network call to the instance
     */
    @Given("That TEI has a {string} event scheduled for {string}")
    public void thatTeiHasAEventScheduledFor(String eventName, String relativeEventDate) throws Exception {
        String eventDateString = Period.toDateString(relativeEventDate);
        Map<String, Object> templateContext = Map.of("data", this,
                "dataTable", new HashMap<String, String>(),
                "occurredAt", "",
                "scheduledAt", toISODateTimeString(eventDateString),
                "programStageId", testIdConverter.getProgramStageId(eventName),
                "eventStatus", "SCHEDULE"
        );
        createEvent(templateContext);
    }

    @Given("I am signed in as a user with role {string}")
    public void iAmSignedInAsAUserWithRole(String role) {
        //TODO: Extend this function to support other user roles
        scenario.log("Signed in as a Superuser user with access to update the metadata");
    }

    @Given("Clears cache")
    public void clearsCache() throws Exception {
        String endpoint = "api/maintenance?cacheClear=true&appReload=true";
        String response = dhis2HttpClient.doPost(endpoint);
        LOGGER.info("Response {}", response);
        scenario.log("Cleared cache");
    }

    /**
     * I wait for {int} seconds.
     *
     * <pre>
     * Given I wait for 1 second.
     * </pre>
     *
     * This pauses execution for the number of seconds.
     *
     * <p>Sometimes, the step just performed on DHIS may take a while to "settle";
     * i.e. the DB (or some other component) may still be working. Use this
     * step to give some time for existing processes to wrap up.</p>
     *
     * @param seconds The number of seconds to wait for
     */
    @Given("I wait for {int} second")
    @Given("I wait for {int} seconds")
    public void waitUntilDBTriggerCompletion(int seconds) throws Exception{
        Thread.sleep(1000 * seconds);
    }

    private List<AttributeInfo> getAttributes(Map<String, String> data) {
        List<AttributeInfo> returnList = new ArrayList<>();
        for (String currentKey : data.keySet()) {
            String value = data.get(currentKey);
            AttributeInfo currentAttribute = new AttributeInfo().withAttribute(currentKey).withValue(value);
            returnList.add(currentAttribute);
        }
        return returnList;
    }

    private String executeJob(String jobId) throws Exception {
        String response = dhis2HttpClient.doPost("api/jobConfigurations/" + jobId + "/execute");
        LOGGER.info("Response {}", response);
        return response;
    }

    private String getOptionNameFromCode(String code) throws Exception {
        String response = dhis2HttpClient.doGet("api/options?paging=false&fields=name&filter=code:eq:" + code);
        JsonNode rootNode = MAPPER.readTree(response);
        return rootNode.get("options").get(0).get("name").asText();
    }

    private void createEvent(Map<String, Object> templateContext) throws Exception {
        this.currentEventId = dhis2HttpClient.getUniqueDhis2Id();

        String response = dhis2HttpClient.doPost("api/tracker?async=false&mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE", "create_event_tei.tpl.json", templateContext);

        LOGGER.info("Response {}", response);
        scenario.log("Created new event with Id:" + currentEventId + " for the TEI with Id:" + currentTeiId);
    }

    private Map<String, String> getAnalyticData(String dimensionItemId, String dimensionItemName, String periodType) throws Exception {
        String orgUnit = this.currentOrgUnitId;
        String periods = periodType.equalsIgnoreCase("months") ? "LAST_12_MONTHS;THIS_MONTH" : "LAST_4_QUARTERS;THIS_QUARTER";
        String endpoint = "api/analytics.json";
        String params = "?dimension=dx:" + dimensionItemId + "&dimension=pe:" + periods + "&dimension=ou:" + orgUnit + "&tableLayout=true&columns=dx;ou&rows=pe";
        String response = dhis2HttpClient.doGet(endpoint + params);
        JsonNode rootNode = MAPPER.readTree(response);
        ArrayNode periodValues = (ArrayNode) rootNode.get("rows");
        Map<String, String> actualPeriodValues = new HashMap<>();
        for (JsonNode dataValue : periodValues) {
            String key = dataValue.get(0).asText();
            String value = dataValue.get(4).asText().isEmpty() ? "0" : dataValue.get(4).asText();
            actualPeriodValues.put(key, value);
        }
        LOGGER.info("Response {}", response);
        scenario.log(dimensionItemName + ": " + dimensionItemId + " for the `" + periods + "` in Organisation Unit:" + orgUnit + " is " + actualPeriodValues);
        return actualPeriodValues;
    }
}
