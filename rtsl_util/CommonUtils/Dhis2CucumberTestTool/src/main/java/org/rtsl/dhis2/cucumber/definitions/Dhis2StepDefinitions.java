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

    String currentFacilityId = null;
    String currentEnrollmentId = null;
    String currentTeiId = null;
    String currentEventId = null;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String getCurrentFacilityId() {
        return currentFacilityId;
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
    public void i_create_a_new_facility() throws Exception {
        Map<String, String> newOrganisationUnit = organisationUnit.createFacility(scenario);
        this.currentFacilityId = newOrganisationUnit.get("organisationUnitId");
        LOGGER.info("created OrgUnit: <{}> <{}>", currentFacilityId, newOrganisationUnit.get("organisationUnitName"));
        scenario.log("Created new OrgUnit with Id:" + currentFacilityId + " and Name:" + newOrganisationUnit.get("organisationUnitName"));
    }

    @Given("I assign the current user to the current orgUnit")
    public void i_assign_the_current_user_to_the_current_org_unit() throws Exception {
        //
        // Links the current User to the current facility
        // 
        String currentUserId = dhis2HttpClient.getCurrentUserId();
        String baseUserJson = dhis2HttpClient.doGet("api/users/" + currentUserId + "?fields=id,name,organisationUnits,userGroups,userRoles,dataViewOrganisationUnits,teiSearchOrganisationUnits");
        JsonNode rootNode = MAPPER.readTree(baseUserJson);
        ObjectNode newId = MAPPER.createObjectNode();
        newId.put("id", currentFacilityId);
        ArrayNode organisationUnits = (ArrayNode) rootNode.get("organisationUnits");
        organisationUnits.add(newId);
        ArrayNode dataViewOrganisationUnits = (ArrayNode) rootNode.get("dataViewOrganisationUnits");
        dataViewOrganisationUnits.add(newId);
        ArrayNode teiSearchOrganisationUnits = (ArrayNode) rootNode.get("teiSearchOrganisationUnits");
        teiSearchOrganisationUnits.add(newId);
        String modifiedJson = MAPPER.writeValueAsString(rootNode);
        dhis2HttpClient.doPutWithBody("api/users/" + currentUserId + "?mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE", modifiedJson);

    }

    @Given("I register that Facility for program {string}")
    public void i_register_that_facility(String string) throws Exception {
        String programName = testIdConverter.getProgramId(string);
        String baseProgramJson = dhis2HttpClient.doGet("api/programs/" + programName);
        JsonNode rootNode = MAPPER.readTree(baseProgramJson);
        ArrayNode organisationUnits = (ArrayNode) rootNode.get("organisationUnits");
        ObjectNode facilityId = MAPPER.createObjectNode();
        facilityId.put("id", currentFacilityId);
        organisationUnits.add(facilityId);
        String modifiedJson = MAPPER.writeValueAsString(rootNode);
        String response = dhis2HttpClient.doPutWithBody(
                "api/programs/" + programName + "?mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                modifiedJson);
        LOGGER.info("Response {}", response);
        scenario.log("Current facility: " + facilityId.get("id") + " has been assigned to the program:" + programName);
    }

    @Given("I create a new TEI for this OrgUnit with the following attributes")
    @Given("I create a new Patient on {string} for this Facility with the following attributes")
    public void i_create_a_new_patient_on_for_this_facility_with_the_following_attributes(String string, Map<String, String> dataTable) throws Exception {
        Map<String, String> newTei = trackedEntityInstance.create(dataTable, currentFacilityId, string);
        this.currentTeiId = newTei.get("id");
        this.currentEnrollmentId = newTei.get("enrollmentId");
        scenario.log("Created new TEI with Id:" + currentTeiId + " and Enrollment with Id:" + currentEnrollmentId);
    }

    @Given("That patient has a {string} event on {string} with following data")
    public void thatPatientHasAEventOnWithFollowingData(String eventName, String eventDateString, Map<String, String> dataTable) throws Exception {
        thatPatientHasAEventOnWhichWasScheduledOnWithFollowingData(eventName, eventDateString, eventDateString, dataTable);
    }

    @Given("That patient has a {string} event on {string} which was scheduled on {string} with following data")
    public void thatPatientHasAEventOnWhichWasScheduledOnWithFollowingData(String eventName, String eventDateString, String scheduledDateString,  Map<String, String> dataTable) throws Exception {
        Map<String, String> convertedDataTable = new HashMap<>();

        for (String dataElement : dataTable.keySet()) {
            String dataElementId;
            if (dataElement.equals("HTN - Blood sugar reading")) {
                String unitValue = dataTable.get("HTN - Blood sugar unit");
                String bloodSugarTypeValue = dataTable.get("HTN - Type of diabetes measure?");
                String bloodSugarType = getOptionNameFromCode(bloodSugarTypeValue);
                if (unitValue.isEmpty()) {
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
    public void export_the_analytics() throws Exception {
        String exportAnalyticsJobId = testIdConverter.getJobConfigurationId("Matview Refresh");
        String jobStatus;
        String lastRuntimeExecution;
        String response;

        executeJob(exportAnalyticsJobId);
        do {
            // Loop until job is finished
            response = dhis2HttpClient.doGet("api/jobConfigurations/" + exportAnalyticsJobId);
            JsonNode jobConfigurations = MAPPER.readTree(response);
            jobStatus = jobConfigurations.get("jobStatus").asText();
            lastRuntimeExecution = jobConfigurations.get("lastRuntimeExecution").asText();
        } while (jobStatus.equals("RUNNING"));
        LOGGER.info("Response {}", response);
        scenario.log("Analytics job with Id:" + exportAnalyticsJobId + " took " + lastRuntimeExecution + " time to complete.");
    }

    @Given("Run the Hypertension data aggregation")
    public void run_the_hypertension_data_aggregation() throws Exception {
        String dataAggregationJobId = testIdConverter.getJobConfigurationId("ADEX - Hypertension dashboard");
        String jobStatus;
        String lastRuntimeExecution;
        String response;

        executeJob(dataAggregationJobId);
        do {
            // Loop until job is finished
            response = dhis2HttpClient.doGet("api/jobConfigurations/" + dataAggregationJobId);
            JsonNode jobConfigurations = MAPPER.readTree(response);
            jobStatus = jobConfigurations.get("jobStatus").asText();
            lastRuntimeExecution = jobConfigurations.get("lastRuntimeExecution").asText();
        } while (jobStatus.equals("RUNNING"));
        LOGGER.info("Response {}", response);
        scenario.log("Data exchange and aggregation job with Id:" + dataAggregationJobId + " took " + lastRuntimeExecution + " time to complete.");
    }

    @Then("The value of PI {string} should be")
    public void the_value_of_pi_should_be(String string, Map<String, String> dataTable) throws Exception {
        String programIndicatorId = testIdConverter.getProgramIndicatorId(string);
        String orgUnit = this.currentFacilityId;
        String periods = "LAST_12_MONTHS;THIS_MONTH";
        String endpoint = "api/analytics.json";
        String params = "?dimension=dx:" + programIndicatorId + "&dimension=pe:" + periods + "&filter=ou:" + orgUnit;
        String response = dhis2HttpClient.doGet(endpoint + params);
        JsonNode rootNode = MAPPER.readTree(response);
        ArrayNode periodValues = (ArrayNode) rootNode.get("rows");
        Map<String, String> actualPeriodValues = new HashMap<>();
        for (JsonNode dataValue : periodValues) {
            String key = dataValue.get(1).asText();
            String value = dataValue.get(2).asText();
            actualPeriodValues.put(key, value);
        }
        for (String period : dataTable.keySet()) {
            assertEquals(dataTable.get(period),
                    actualPeriodValues.get(period),
                    "Program Indicator: <" + programIndicatorId + "> for the <" + period + "> in Organisation Unit:<" + orgUnit + ">");
        }
        LOGGER.info("Response {}", response);
        scenario.log("Program Indicator: " + programIndicatorId + "for the " + periods + " in Organisation Unit:" + orgUnit + "is " + actualPeriodValues);
    }

    @Given("That patient was updated on {string} with the following attributes")
    public void thatPatientWasUpdatedOnWithTheFollowingAttributes(String visitDate, Map<String, String> dataTable) throws Exception {
        trackedEntityInstance.update(dataTable, currentFacilityId, this.currentTeiId);
        scenario.log("Created new TEI with Id:" + currentTeiId + " updated");
    }

    @Given("That patient has a {string} event scheduled for {string}")
    public void thatPatientHasAEventScheduledFor(String eventName, String eventDateString) throws Exception{
        Map<String, Object> templateContext = Map.of("data", this,
                "dataTable", new HashMap<String, String>(),
                "occurredAt", "",
                "scheduledAt", toISODateTimeString(eventDateString),
                "programStageId", testIdConverter.getProgramStageId(eventName),
                "eventStatus", "SCHEDULE"
        );
        createEvent(templateContext);
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
        this.currentEventId = dhis2HttpClient.getGenerateUniqueId();

        String response = dhis2HttpClient.doPost("api/tracker?async=false&mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE", "create_event_tei.tpl.json", templateContext);

        LOGGER.info("Response {}", response);
        scenario.log("Created new event with Id:" + currentEventId + " for the TEI with Id:" + currentTeiId);
    }
}
