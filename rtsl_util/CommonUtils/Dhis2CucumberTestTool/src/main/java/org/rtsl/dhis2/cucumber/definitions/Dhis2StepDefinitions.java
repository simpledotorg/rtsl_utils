package org.rtsl.dhis2.cucumber.definitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.hisp.dhis.api.model.v40_2_2.AttributeInfo;
import org.rtsl.dhis2.cucumber.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.Dhis2IdConverter;
import org.rtsl.dhis2.cucumber.Factories.OrganisationUnit;
import org.rtsl.dhis2.cucumber.Factories.TrackedEntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Dhis2StepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitions.class);

    @Inject
    @Named("testClient")
    private Dhis2HttpClient dhis2HttpClient;

    @Inject
    @Named("testIdConverter")
    private Dhis2IdConverter testIdConverter;

    @Inject
    @Named("organisationUnitFactory")
    private OrganisationUnit organisationUnitFactory;

    @Inject
    @Named("trackedEntityInstanceFactory")
    private TrackedEntityInstance trackedEntityInstanceFactory;

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
        Map<String, String> newOrganisationUnit = organisationUnitFactory.createFacility();
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
    public void i_register_that_facility(String programName) throws Exception {
        String baseProgramJson = dhis2HttpClient.doGet("api/programs/pMIglSEqPGS");
        JsonNode rootNode = MAPPER.readTree(baseProgramJson);
        ArrayNode organisationUnits = (ArrayNode) rootNode.get("organisationUnits");
        ObjectNode facilityId = MAPPER.createObjectNode();
        facilityId.put("id", currentFacilityId);
        organisationUnits.add(facilityId);
        String modifiedJson = MAPPER.writeValueAsString(rootNode);
        String response = dhis2HttpClient.doPutWithBody(
                "api/programs/pMIglSEqPGS?mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                modifiedJson);
        LOGGER.info("Response {}", response);
        scenario.log("Current facility: " + facilityId.get("id") + " has been assigned to the program:" + programName);
    }

    @Given("I create a new TEI for this OrgUnit with the following characteristics")
    @Given("I create a new Patient on {string} for this Facility with the following characteristics")
    public void i_create_a_new_patient_on_for_this_facility_with_the_following_characteristics(String string, Map<String, String> dataTable) throws Exception {
        Map<String, String> newTei = trackedEntityInstanceFactory.create(dataTable, currentFacilityId, string);
        this.currentTeiId = newTei.get("id");
        this.currentEnrollmentId = newTei.get("enrollmentId");
        scenario.log("Created new TEI with Id:" + currentTeiId + " and Enrollment with Id:" + currentEnrollmentId);
    }

    @Given("That patient visited for Hypertension on {string} with Blood Pressure reading {int}:{int}")
    public void that_patient_visited_for_hypertension_on_with_blood_pressure_reading(String string, Integer int1, Integer int2) throws Exception {
        Map<String, Integer> convertedDataTable = Map.of("IxEwYiq1FTq", int1, "yNhtHKtKkO1", int2);
        Map<String, Object> templateContext = Map.of("data", this, "dataTable", convertedDataTable);
        this.currentEventId = dhis2HttpClient.getGenerateUniqueId();
        String response = dhis2HttpClient.doPost("api/tracker?async=false&mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE", "create_event_tei.tpl.json", templateContext);

        LOGGER.info("Response {}", response);
        scenario.log("Created new event with Id:" + currentEventId + " for the TEI with Id:" + currentTeiId);
    }

    @Given("Export the analytics")
    public void export_the_analytics() throws Exception {
        String response = dhis2HttpClient.doPost("api/resourceTables/analytics");
        JsonNode rootNode = MAPPER.readTree(response);
        String analyticsJobId = rootNode.get("response").get("relativeNotifierEndpoint").asText();
        // Sleep until job is finished
        Thread.sleep(10000);
        LOGGER.info("Response {}", response);
        scenario.log("Export the analytics");
    }

    @Given("Run the Hypertension data aggregation")
    public void run_the_hypertension_data_aggregation() throws Exception {
        String htnDataAggregateObjectId = "DySW3FNj3VR";
        String response = dhis2HttpClient.doGet("api/aggregateDataExchanges/" + htnDataAggregateObjectId + "/exchange");
        // Sleep until aggregation is completed
        Thread.sleep(10000);
        LOGGER.info("Response {}", response);
        scenario.log("Ran data aggregation");
    }

    @Then("The value of PI {string} should be")
    public void the_value_of_pi_should_be(String string, Map<String, String> dataTable) throws Exception {
        String programIndicatorId = testIdConverter.getProgramIndicatorId(string);
        String orgUnit = this.currentFacilityId;
        String period = "LAST_12_MONTHS;THIS_MONTH";
        String endpoint = "api/analytics.json";
        String params = "?dimension=dx:" + programIndicatorId + "&dimension=pe:" + period + "&filter=ou:" + orgUnit;
        String response = dhis2HttpClient.doGet(endpoint + params);
        JsonNode rootNode = MAPPER.readTree(response);
        ArrayNode actualPeriodValues = (ArrayNode) rootNode.get("rows");
        Map<String, String> periodValue = new HashMap<>();
        for (JsonNode dataValue : actualPeriodValues) {
            String key = dataValue.get(1).asText();
            String value = dataValue.get(2).asText();
            periodValue.put(key, value);
        }
        for (String expectedPeriod : dataTable.keySet()) {
            assertEquals(periodValue.get(expectedPeriod), dataTable.get(expectedPeriod));
        }
        LOGGER.info("Response {}", response);
        scenario.log("Program Indicator: " + programIndicatorId + "for the " + period + " in Organisation Unit:" + orgUnit + "is " + actualPeriodValues);
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

}
