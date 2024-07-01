package org.rtsl.dhis2.cucumber.definitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.hisp.dhis.Dhis2;
import org.hisp.dhis.api.model.v40_2_2.*;
import org.hisp.dhis.integration.sdk.api.Dhis2Client;
import org.rtsl.dhis2.cucumber.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.Dhis2IdConverter;
import org.rtsl.dhis2.cucumber.TestUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Dhis2StepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitions.class);

    @Inject
    @Named("testUniqueId")
    private TestUniqueId testUniqueId;

    @Inject
    @Named("dhisClient")
    private Dhis2 dhsi2Client;

    @Inject
    @Named("dhisSdkClient")
    private Dhis2Client dhisSdkClient;

    @Inject
    @Named("testAtomicInt")
    private AtomicInteger testCounter;

    @Inject
    @Named("testClient")
    private Dhis2HttpClient dhis2HttpClient;

    @Inject
    @Named("testIdConverter")
    private Dhis2IdConverter testIdConverter;

    String currentFacilityId = null;
    String currentOrganisationUnitId = null;
    String rootOrganisationUnitId = null;
    String currentEnrollmentId = null;
    String currentTeiId = null;
    String currentEventId = null;
    String parentOrganisationUnitId = "";


    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String getCurrentFacilityId() {
        return currentFacilityId;
    }

    public String getParentOrganisationUnitId() {
        return parentOrganisationUnitId;
    }


    public String getCurrentOrganisationUnitId() {
        return currentOrganisationUnitId;
    }

    public String getRootOrganisationUnitId() {
        return rootOrganisationUnitId;
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

    @Given("I have {int} cukes in my belly")
    public void i_have_n_cukes_in_my_belly(Integer cukes) {
        testCounter.addAndGet(cukes);
        LOGGER.info("TEST: <{}> <{}> <{}>", cukes, testCounter.hashCode(), this);
    }

    private Scenario scenario;

    @Before
    public void setup(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("I create a new Facility")
    @Given("I create a new OrgUnit")
    public void i_create_a_new_facility() throws Exception {
        int level = 0;
        while (level < 5) {
            level = level + 1;
            String organisationUnitId = dhis2HttpClient.getGenerateUniqueId();
            if (level == 1) {
                this.rootOrganisationUnitId = organisationUnitId;
            } else {
                this.parentOrganisationUnitId = currentOrganisationUnitId;
            }
            this.currentOrganisationUnitId = organisationUnitId;

            Map<String, Object> organisationUnitTemplateContext = Map.of(
                    "data", this,
                    "organisationUnitName", testUniqueId.get() +"_"+ level,
                    "organisationUnitShortName", testUniqueId.get() +"_"+ level,
                    "organisationUnitOpeningDate", "2024-07-01T00:00:00.000",
                    "organisationUnitLevel", level
            );
            String response = dhis2HttpClient.doPost(
                    "api/organisationUnits",
                    "create_organisation_unit.tpl.json",
                    organisationUnitTemplateContext);
            LOGGER.info("Response {}", response);
        }
        this.currentFacilityId = currentOrganisationUnitId;
        LOGGER.info("created OrgUnit: <{}> <{}>", currentFacilityId, testUniqueId.get() +"_"+ level);
        scenario.log("Created new OrgUnit with Id:" + currentFacilityId + " and Name:" + testUniqueId.get() +"_"+ level);
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

        Map<String, Object> templateContext = Map.of(
                "data", this,
                "programName", programName);

        String response = dhis2HttpClient.doPutWithTemplate(
                "api/programs/pMIglSEqPGS?mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                "register_facility_to_program.tpl.json",
                templateContext);

        LOGGER.info("Response {}", response);
    }

    @Given("I create a new TEI for this OrgUnit with the following characteristics")
    @Given("I create a new Patient for this Facility with the following characteristics")
    public void i_create_a_new_patient_for_this_facility_with_the_following_characteristics(Map<String, String> dataTable) throws Exception {
        Map<String, String> convertedDataTable = testIdConverter.convertTeiAttributes(dataTable);
        this.currentEnrollmentId = dhis2HttpClient.getGenerateUniqueId();
        this.currentTeiId = dhis2HttpClient.getGenerateUniqueId();
        convertedDataTable.remove("enrollmentDate");
        Map<String, Object> templateContext = Map.of(
                "data", this,
                "dataTable", convertedDataTable);

        String response = dhis2HttpClient.doPost(
                "api/tracker?async=false&mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                "create_and_enroll_tei.tpl.json",
                templateContext);

        LOGGER.info("Response {}", response);
        scenario.log("Created new TEI with Id:" + currentTeiId + " and Enrollment with Id:" + currentEnrollmentId);
    }

    @Given("That patient visited for Hypertension on {string} with Blood Pressure reading {int}:{int}")
    public void that_patient_visited_for_hypertension_on_with_blood_pressure_reading(String string, Integer int1, Integer int2) throws Exception {
        Map<String, Integer> convertedDataTable = Map.of("IxEwYiq1FTq", int1, "yNhtHKtKkO1", int2);
        Map<String, Object> templateContext = Map.of(
                "data", this,
                "dataTable", convertedDataTable);
        this.currentEventId = dhis2HttpClient.getGenerateUniqueId();
        String response = dhis2HttpClient.doPost(
                "api/tracker?async=false&mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                "create_event_tei.tpl.json",
                templateContext);

        LOGGER.info("Response {}", response);
        scenario.log("Created new event with Id:" + currentEventId + " for the TEI with Id:" + currentTeiId);
    }

    //@Given("I create a new Patient for this Facility with the following characteristics")
    public void i_create_a_new_patient_for_this_facility_with_the_following_characteristics_OLD(Map<String, String> dataTable) {

        LOGGER.info("Creating patient for data: <{}> ", dataTable);
        LOGGER.info("{}", new Date().toString());

        // Creating a new TEI
        TrackedEntityInfo newTEI = new TrackedEntityInfo();
        newTEI.setOrgUnit(currentFacilityId); // todo: use current facility
        newTEI.setTrackedEntityType("MCPQUTHX1Ze"); // Person

        // Creating new Enrollment
        EnrollmentInfo newEnrollment = new EnrollmentInfo();
        newEnrollment.setOrgUnit(currentFacilityId); // todo: use current facility
        newEnrollment.setProgram("pMIglSEqPGS"); // Hypertension & Diabetes
        newEnrollment.setEnrolledAt(new Date());
        newEnrollment.setOccurredAt(new Date());
        newEnrollment.setAttributes(getAttributes(dataTable));
        // TODO

        newTEI.setEnrollments(Arrays.asList(newEnrollment));

        TrackerImportReport trackerImportReport = dhisSdkClient.post("tracker")
                .withResource(new Body().withTrackedEntities(Arrays.asList(newTEI
                )))
                .withParameter("async", "false")
                .transfer()
                .returnAs(TrackerImportReport.class);
        /**/

        LOGGER.info("{}", trackerImportReport.getBundleReport().get().toString());
        //dhisSdkClient.
        // TODO
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
