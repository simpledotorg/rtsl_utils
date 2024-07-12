package org.rtsl.dhis2.cucumber.factories;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.rtsl.dhis2.cucumber.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.Dhis2IdConverter;
import org.rtsl.dhis2.cucumber.definitions.Dhis2StepDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import static org.rtsl.dhis2.cucumber.Helper.convertToISODateTimeString;

public class TrackedEntityInstance {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitions.class);
    @Inject
    @Named("testClient")
    private Dhis2HttpClient dhis2HttpClient;

    @Inject
    @Named("testIdConverter")
    private Dhis2IdConverter testIdConverter;

    String enrollmentId = null;
    String teiId = null;
    String orgUnitId = null;
    String enrolledAt = null;

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public String getTeiId() {
        return teiId;
    }
    public String getOrgUnitId() { return orgUnitId; }

    public String getEnrolledAt() { return enrolledAt; }


    public Map<String, String> create(Map<String, String> dataTable, String orgUnitId, String enrolledAt) throws Exception{
        Map<String, String> convertedDataTable = testIdConverter.convertTeiAttributes(dataTable);
        this.enrollmentId = dhis2HttpClient.getGenerateUniqueId();
        this.teiId = dhis2HttpClient.getGenerateUniqueId();
        this.orgUnitId = orgUnitId;
        this.enrolledAt = convertToISODateTimeString(enrolledAt);
        Map<String, Object> templateContext = Map.of("data", this, "dataTable", convertedDataTable);
        String response = dhis2HttpClient.doPost("api/tracker?async=false&mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                "create_and_enroll_tei.tpl.json",
                templateContext);

        LOGGER.info("Response {}", response);
        return  Map.of("id", teiId, "enrollmentId", enrollmentId);
    }
}
