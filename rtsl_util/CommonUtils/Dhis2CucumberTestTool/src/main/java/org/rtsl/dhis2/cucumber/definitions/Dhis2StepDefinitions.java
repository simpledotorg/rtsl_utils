package org.rtsl.dhis2.cucumber.definitions;

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
import org.hisp.dhis.api.model.v40_2_2.AttributeInfo;
import org.rtsl.dhis2.cucumber.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.Dhis2IdConverter;
import org.rtsl.dhis2.cucumber.Period;
import org.rtsl.dhis2.cucumber.factories.OrganisationUnit;
import org.rtsl.dhis2.cucumber.factories.TrackedEntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.rtsl.dhis2.cucumber.Helper.toISODateTimeString;

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

    public String getCurrentOrgUnitId() {
        return currentOrgUnitId;
    }

    public String getCurrentEventId() {
        return currentEventId;
    }

    public String getCurrentEnrollmentId() {
        return currentEnrollmentId;
    }

    public String getCurrentTeiId() {
        return currentTeiId;
    }

    private Scenario scenario;

    @Before
    public void setup(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("I create a new Facility")
    @Given("I create a new OrgUnit")
    public void iCreateANewOrganisationUnitAtLevel() throws Exception {
        iCreateANewOrganisationUnitAtLevel(5);
    }

    @Given("I create a new organisationUnit at level {int}")
    public void iCreateANewOrganisationUnitAtLevel(int level) throws Exception {
        Map<String, String> newOrganisationUnit = organisationUnit.createOrganisationUnit(level);
        this.currentOrgUnitId = newOrganisationUnit.get("organisationUnitId");
        LOGGER.info("created OrgUnit: <{}> <{}>", currentOrgUnitId, newOrganisationUnit.get("organisationUnitName"));
        scenario.log("Created new OrgUnit with Id:" + currentOrgUnitId + " and Name:" + newOrganisationUnit.get("organisationUnitName"));
    }

    @And("I have access to an organisation unit at level {int}")
    public void iHaveAccessToAnOrganisationUnitAtLevel(int level) throws Exception {
        iCreateANewOrganisationUnitAtLevel(level);
        iAssignTheCurrentUserToTheCurrentOrgUnit();
    }

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

    @Given("I register that Facility for program {string}")
    @Given("I register that organisation unit for program {string}")
    public void iRegisterThatOrganisationUnit(String string) throws Exception {
        String programName = testIdConverter.getProgramId(string);
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

    @Given("That TEI has a {string} event on {string} with following data")
    public void thatTeiHasAEventOnWithFollowingData(String eventName, String relativeEventDate, Map<String, String> dataTable) throws Exception {
        thatTeiHasAEventOnWhichWasScheduledOnWithFollowingData(eventName, relativeEventDate, relativeEventDate, dataTable);
    }

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

    @Given("Export the analytics")
    @Given("I export the analytics")
    public void exportTheAnalytics() throws Exception {
        String exportAnalyticsJobId = testIdConverter.getJobConfigurationId("Matview Refresh");
        String jobStatus;
        String lastRuntimeExecution;
        String response;
        String lastExecutedStatus;

        // Wait 1 sec for the trigger logic to complete
        Thread.sleep(1000);

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

    @Given("Run the Hypertension data aggregation")
    @Given("I run the hypertension data aggregation")
    public void runTheHypertensionDataAggregation() throws Exception {
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

    @Given("That TEI was updated on {string} with the following attributes")
    public void thatTeiWasUpdatedOnWithTheFollowingAttributes(String relativeVisitDate, Map<String, String> dataTable) throws Exception {
        String visitDate = Period.toDateString(relativeVisitDate);
        trackedEntityInstance.update(dataTable, currentOrgUnitId, this.currentTeiId, visitDate);
        scenario.log("Created new TEI with Id:" + currentTeiId + " updated");
    }

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

    @Given("Clears cache")
    public void clearsCache() throws Exception {
        String endpoint = "api/maintenance?cacheClear=true&appReload=true";
        String response = dhis2HttpClient.doPost(endpoint);
        LOGGER.info("Response {}", response);
        scenario.log("Cleared cache");
    }
}
