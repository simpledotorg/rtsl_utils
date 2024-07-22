package org.rtsl.dhis2.cucumber.factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.rtsl.dhis2.cucumber.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.TestUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class OrganisationUnit {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationUnit.class);
    private static String DISTRICT_ID = "";
    private static String ROOT_ORG_UNIT_ID = "";

    public String getRootOrgUnitId() {
        return ROOT_ORG_UNIT_ID;
    }

    @Inject
    @Named("testClient")
    private Dhis2HttpClient dhis2HttpClient;

    @Inject
    @Named("testUniqueId")
    private TestUniqueId testUniqueId;

    public Map<String, String> createFacility() throws Exception {
        int organisationUnitLevel;
        String parentOrganisationUnitId = DISTRICT_ID;

        Map<String, String> newOrganisationUnit = null;
        if (DISTRICT_ID == null || DISTRICT_ID.trim().isEmpty()) {
            organisationUnitLevel = 1;
        } else {
            organisationUnitLevel = 4;
        }
        while (organisationUnitLevel <= 5) {
            newOrganisationUnit = create(organisationUnitLevel, parentOrganisationUnitId);
            if (organisationUnitLevel == 3) {
                DISTRICT_ID = newOrganisationUnit.get("organisationUnitId");
            } else if (organisationUnitLevel == 1) {
                ROOT_ORG_UNIT_ID = newOrganisationUnit.get("organisationUnitId");
            }
            parentOrganisationUnitId = newOrganisationUnit.get("organisationUnitId");
            ++organisationUnitLevel;
        }
        return newOrganisationUnit;
    }

    public Map<String, String> create(int organisationUnitLevel, String parentOrganisationUnitId) throws Exception {
        String newOrganisationUnitId = dhis2HttpClient.getGenerateUniqueId();
        String newOrganisationUnitName = testUniqueId.get() + "_" + organisationUnitLevel;
        Map<String, Object> organisationUnitTemplateContext = Map.of(
                "data", this,
                "organisationUnitName", newOrganisationUnitName,
                "organisationUnitShortName", newOrganisationUnitName,
                "organisationUnitOpeningDate", "2023-07-01T00:00:00.000",
                "organisationUnitLevel", organisationUnitLevel,
                "organisationUnitId", newOrganisationUnitId,
                "parentOrganisationUnitId", parentOrganisationUnitId);
        String response = dhis2HttpClient.doPost(
                "api/organisationUnits",
                "create_organisation_unit.tpl.json",
                organisationUnitTemplateContext);
        LOGGER.info("Response {}", response);
        return Map.of("organisationUnitId", newOrganisationUnitId, "organisationUnitName", newOrganisationUnitName);
    }

    public String getAncestorId(String childId, int ancestorLevel) throws Exception {
        String response = dhis2HttpClient.doGet("api/organisationUnits/"+childId+"?fields=ancestors[id,level],level");
        ObjectMapper objectMapper = new ObjectMapper();
        String ancestorId = "";
        JsonNode parsedResponse = objectMapper.readTree(response);
        ArrayNode ancestorsNode = (ArrayNode) parsedResponse.get("ancestors");
        for(JsonNode ancestor : ancestorsNode){
            if(ancestor.get("level").asInt() == (ancestorLevel)){
                ancestorId = ancestor.get("id").asText();
            }
        }
        return ancestorId;
    }

    public void delete(int organisationUnitLevel, String parentOrganisationUnitId) throws Exception {
        // TODO
    }

    public void deleteAll() throws Exception {
        // TODO Deletes all org unit along with its dependencies
        // delete(1, getRootOrgUnitId());
    }

}