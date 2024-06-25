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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.hisp.dhis.Dhis2;
import org.hisp.dhis.api.model.v40_2_2.AttributeInfo;
import org.hisp.dhis.api.model.v40_2_2.Body;
import org.hisp.dhis.api.model.v40_2_2.EnrollmentInfo;
import org.hisp.dhis.api.model.v40_2_2.TrackedEntityInfo;
import org.hisp.dhis.api.model.v40_2_2.TrackerImportReport;
import org.hisp.dhis.integration.sdk.api.Dhis2Client;
import org.hisp.dhis.model.OrgUnit;
import org.hisp.dhis.response.object.ObjectResponse;
import org.rtsl.dhis2.cucumber.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.Dhis2IdConverter;
import org.rtsl.dhis2.cucumber.TestUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    String currentFaciliyId = null;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String getCurrentFaciliyId() {
        return currentFaciliyId;
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

        // USING dhis2-java-client
        OrgUnit newFacility = new OrgUnit(null, testUniqueId.get());
        newFacility.setShortName(testUniqueId.get());
        newFacility.setOpeningDate(new Date());
        ObjectResponse response = dhsi2Client.saveOrgUnit(newFacility);
        String newFacilityId = response.getResponse().getUid();
        newFacility.setId(newFacilityId);
        this.currentFaciliyId = newFacilityId;
        LOGGER.info("created OrgUnit: <{}> <{}>", newFacility.getId(), newFacility.getName());
        scenario.log("Created new OrgUnit with Id:" + newFacility.getId() + " and Name:" + newFacility.getName());
    }

    @Given("I assign the current user to the current orgUnit")
    public void i_assign_the_current_user_to_the_current_org_unit() throws Exception {
        //
        // Links the current User to the current facility
        // 
        String currentUserId = dhis2HttpClient.getCurrentUserId();
        String baseUserJson = dhis2HttpClient.doGet("api/users/" + currentUserId + "?fields=id,name,organisationUnits,userRoles,dataViewOrganisationUnits,teiSearchOrganisationUnits");
        JsonNode rootNode = MAPPER.readTree(baseUserJson);
        ObjectNode newId = MAPPER.createObjectNode();
        newId.put("id", currentFaciliyId);
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

        Map<String, Object> templateContext = Map.of(
                "data", this,
                "dataTable", convertedDataTable);

        String response = dhis2HttpClient.doPost(
                "api/tracker?async=false&mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                "create_and_enroll_tei.tpl.json",
                templateContext);

        LOGGER.info("Response {}", response);
    }

    //@Given("I create a new Patient for this Facility with the following characteristics")
    public void i_create_a_new_patient_for_this_facility_with_the_following_characteristics_OLD(Map<String, String> dataTable) {

        LOGGER.info("Creating patient for data: <{}> ", dataTable);
        LOGGER.info("{}", new Date().toString());

        // Creating a new TEI
        TrackedEntityInfo newTEI = new TrackedEntityInfo();
        newTEI.setOrgUnit(currentFaciliyId); // todo: use current facility
        newTEI.setTrackedEntityType("MCPQUTHX1Ze"); // Person

        // Creating new Enrollment
        EnrollmentInfo newEnrollment = new EnrollmentInfo();
        newEnrollment.setOrgUnit(currentFaciliyId); // todo: use current facility
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
