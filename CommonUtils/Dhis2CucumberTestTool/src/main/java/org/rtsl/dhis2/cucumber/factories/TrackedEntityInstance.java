package org.rtsl.dhis2.cucumber.factories;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.rtsl.dhis2.cucumber.utils.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.utils.Dhis2IdConverter;
import org.rtsl.dhis2.cucumber.definitions.Dhis2StepDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.rtsl.dhis2.cucumber.utils.Helper.toISODateTimeString;

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
    private String occurredAt = null;
    public String getEnrollmentId() {
        return enrollmentId;
    }
    public String getTeiId() {
        return teiId;
    }
    public String getOrgUnitId() { return orgUnitId; }

    public String getEnrolledAt() { return enrolledAt; }
    public String getOccurredAt() { return occurredAt; }

    public Map<String, String> create(Map<String, String> dataTable, String orgUnitId, String enrolledAt) throws Exception {
        Map<String, String> convertedDataTable = testIdConverter.convertMetadata(dataTable, "trackedEntityAttribute");
        this.enrollmentId = dhis2HttpClient.getUniqueDhis2Id();
        this.teiId = dhis2HttpClient.getUniqueDhis2Id();
        this.orgUnitId = orgUnitId;
        this.enrolledAt = toISODateTimeString(enrolledAt);
        Map<String, Object> templateContext = Map.of("data", this, "dataTable", convertedDataTable);
        String response = dhis2HttpClient.doPost("api/tracker?async=false&mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                "create_and_enroll_tei.tpl.json",
                templateContext);

        LOGGER.info("Response {}", response);
        return Map.of("id", teiId, "enrollmentId", enrollmentId);
    }

    public void update(Map<String, String> dataTable, String orgUnitId, String teiId, String occurredAt) throws Exception {
        Map<String, String> convertedDataTable = testIdConverter.convertMetadata(dataTable, "trackedEntityAttribute");
        this.teiId = teiId;
        this.orgUnitId = orgUnitId;
        this.occurredAt = toISODateTimeString(occurredAt);
        Map<String, Object> templateContext = Map.of("data", this, "dataTable", convertedDataTable);
        String response = dhis2HttpClient.doPost("api/tracker?async=false&mergeMode=MERGE&importStrategy=CREATE_AND_UPDATE",
                "update_tei.tpl.json",
                templateContext);

        LOGGER.info("Response {}", response);
    }
}
