package org.rtsl.dhis2.cucumber.factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.cucumber.java.Scenario;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.rtsl.dhis2.cucumber.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.TestUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;

import static org.rtsl.dhis2.cucumber.Helper.PROGRAM_CONSTANTS;
import static org.rtsl.dhis2.cucumber.Helper.toISODateTimeString;


public class OrganisationUnit {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationUnit.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static String districtId = "";

    private static String testRootOrganisationUnitId;

    public String getTestRootOrganisationUnitId() throws Exception {
        return testRootOrganisationUnitId;
    }

    @Inject
    @Named("testClient")
    private Dhis2HttpClient dhis2HttpClient;

    @Inject
    @Named("testUniqueId")
    private TestUniqueId testUniqueId;

    public TestUniqueId getTestUniqueId() {
        return testUniqueId;
    }

    public void createRoot() throws Exception {
        String id = dhis2HttpClient.getGenerateUniqueIds(1).get(0).asText();
        String rootOrganisationUnitName = getProgramConstant("rootOrganisationUnitName");
        if (getOrganisationUnitId(rootOrganisationUnitName).isBlank()) {
            testRootOrganisationUnitId = create(id, rootOrganisationUnitName, rootOrganisationUnitName, PROGRAM_CONSTANTS.get("openingDatesOfOrganisationUnit").asText(), 1, "");
        }
    }

    public void createOrganisationUnitHierarchy() throws Exception {
        testRootOrganisationUnitId = getOrganisationUnitId(getProgramConstant("rootOrganisationUnitName"));
        if (testRootOrganisationUnitId.isBlank()) {
            createRoot();
        }
        ArrayNode ids = dhis2HttpClient.getGenerateUniqueIds(2);
        String stateOrganisationUnitName = getProgramConstant("stateNamePrefix") + testUniqueId.hashCode();
        String stateOrganisationUnitId = ids.get(0).asText();
        String districtOrganisationUnitName = getProgramConstant("districtNamePrefix") + testUniqueId.hashCode();
        String districtOrganisationUnitId = ids.get(1).asText();

        create(stateOrganisationUnitId, stateOrganisationUnitName, stateOrganisationUnitName, getProgramConstant("openingDatesOfOrganisationUnit"), 2, testRootOrganisationUnitId);
        districtId = create(districtOrganisationUnitId, districtOrganisationUnitName, districtOrganisationUnitName, getProgramConstant("openingDatesOfOrganisationUnit"), 3, ids.get(0).asText());

    }

    public Map<String, String> createFacility(Scenario scenario) throws Exception {
        if (districtId.isBlank()) {
            createOrganisationUnitHierarchy();
        }
        ArrayNode ids = dhis2HttpClient.getGenerateUniqueIds(2);
        String blockOrganisationUnitName = getProgramConstant("blockNamePrefix") + testUniqueId.hashCode();
        String blockId = create(ids.get(0).asText(), blockOrganisationUnitName, blockOrganisationUnitName, getProgramConstant("openingDatesOfOrganisationUnit"), 4, districtId);

        String facilityOrganisationUnitName = getProgramConstant("facilityNamePrefix") + testUniqueId.hashCode();
        String facilityId = create(ids.get(1).asText(), facilityOrganisationUnitName, facilityOrganisationUnitName, getProgramConstant("openingDatesOfOrganisationUnit"), 5, blockId);
        return Map.of("organisationUnitId", facilityId, "organisationUnitName", facilityOrganisationUnitName);
    }


    public String create(String id, String name, String shortName, String openingDate, int level, String parentId) throws Exception {
        Map<String, Object> organisationUnitTemplateContext = Map.of(
                "data", this,
                "organisationUnitId", id,
                "organisationUnitName", name,
                "organisationUnitShortName", shortName,
                "organisationUnitOpeningDate", toISODateTimeString(openingDate),
                "organisationUnitLevel", level,
                "parentOrganisationUnitId", parentId);
        String response = dhis2HttpClient.doPost(
                "api/organisationUnits",
                "create_organisation_unit.tpl.json",
                organisationUnitTemplateContext);
        LOGGER.info("Response {}", response);
        return id;
    }

    public String getAncestorId(String childId, int ancestorLevel) throws Exception {
        String response = dhis2HttpClient.doGet("api/organisationUnits/" + childId + "?fields=ancestors[id,level],level");
        String ancestorId = "";
        JsonNode parsedResponse = MAPPER.readTree(response);
        ArrayNode ancestorsNode = (ArrayNode) parsedResponse.get("ancestors");
        for (JsonNode ancestor : ancestorsNode) {
            if (ancestor.get("level").asInt() == (ancestorLevel)) {
                ancestorId = ancestor.get("id").asText();
            }
        }
        return ancestorId;
    }

    private String getProgramConstant(String key)
    {
        String value = PROGRAM_CONSTANTS.get(key).asText();
        if(value == null){ value = ""; }
        return value;
    }

    public String getOrganisationUnitId(String organisationUnitName) throws Exception {
        String response = dhis2HttpClient.doGet("api/organisationUnits?filter=level:eq:1&fields=id,level,name");
        JsonNode parsedResponse = MAPPER.readTree(response);
        String rootOrganisationUnitId = "";
        ArrayNode organisationUnits = (ArrayNode) parsedResponse.get("organisationUnits");
        for (JsonNode organisationUnit : organisationUnits) {
            if (organisationUnit.get("name").asText().equals(organisationUnitName)) {
                rootOrganisationUnitId = organisationUnit.get("id").asText();
                break;
            }
        }
        return rootOrganisationUnitId;
    }

    public void delete(int organisationUnitLevel, String parentOrganisationUnitId) throws Exception {
        // TODO
    }

    public void deleteAll() throws Exception {
        // TODO Deletes all org unit along with its dependencies
        // delete(1, getRootOrgUnitId());
    }

}
