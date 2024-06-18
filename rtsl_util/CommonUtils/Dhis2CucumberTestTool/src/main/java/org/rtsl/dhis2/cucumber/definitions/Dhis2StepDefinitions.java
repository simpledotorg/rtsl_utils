package org.rtsl.dhis2.cucumber.definitions;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.Version;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.hisp.dhis.Dhis2;
import org.hisp.dhis.api.model.v40_2_2.AttributeInfo;
import org.hisp.dhis.api.model.v40_2_2.Body;
import org.hisp.dhis.api.model.v40_2_2.EnrollmentInfo;
import org.hisp.dhis.api.model.v40_2_2.OrganisationUnit;
import org.hisp.dhis.api.model.v40_2_2.Program;
import org.hisp.dhis.api.model.v40_2_2.TrackedEntityInfo;
import org.hisp.dhis.api.model.v40_2_2.TrackerImportReport;
import org.hisp.dhis.integration.sdk.api.Dhis2Client;
import org.hisp.dhis.model.OrgUnit;
import org.hisp.dhis.response.object.ObjectResponse;
import org.rtsl.dhis2.cucumber.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.TestUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dhis2StepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitions.class);

    HttpClientConnectionManager httpClientManager;

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

    String currentFaciliyId = null;

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
    public void i_create_a_new_facility() {

        OrgUnit newFacility = new OrgUnit(null, testUniqueId.get());
        newFacility.setShortName(testUniqueId.get());
        newFacility.setOpeningDate(new Date());
        ObjectResponse response = dhsi2Client.saveOrgUnit(newFacility);
        String newFacilityId = response.getResponse().getUid();
        newFacility.setId(newFacilityId);
        this.currentFaciliyId = newFacilityId;
        LOGGER.info("created Facility: <{}> <{}>", newFacility.getId(), newFacility.getName());
        scenario.log("Created new facility with Id:" + newFacility.getId() + " and Name:" + newFacility.getName());
    }

    @Given("I register that Facility for program {string}")
    public void i_register_that_facility(String programName) throws Exception {

        Map<String, Object> templateContext = Map.of(
                "data", this,
                "programName", programName);

        String response = dhis2HttpClient.doPut(
                "api/programs/pMIglSEqPGS?mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                "register_facility_to_program.tpl.json",
                templateContext);

        LOGGER.info("Response {}", response);
        /*
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_33);
        cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
        Template registerTemplate = cfg.getTemplate("register_facility_to_program.tpl.json");
        Map<String, Object> templateContext = Map.of(
                "data", this,
                "programName", programName);
        StringWriter sw = new StringWriter();
        registerTemplate.process(templateContext, sw);
        String body = sw.toString();
        LOGGER.info(body);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            HttpPut request = new HttpPut("https://dhis2-sandbox2.simple.org/api/programs/pMIglSEqPGS?mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE");
            request.setEntity(new StringEntity(body));
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "Basic YWRtaW46ZGlzdHJpY3Q=");
            try (CloseableHttpResponse response = httpClient.execute(request)) {

            }

        } /**/
    }

    @Given("I create a new Patient for this Facility with the following characteristics")
    public void i_create_a_new_patient_for_this_facility_with_the_following_characteristics(Map<String, String> dataTable) {

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
