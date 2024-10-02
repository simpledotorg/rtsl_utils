package org.rtsl.dhis2.cucumber.factories;

import static org.rtsl.dhis2.cucumber.utils.Helper.toISODateTimeString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rtsl.dhis2.cucumber.utils.Dhis2HttpClient;
import org.rtsl.dhis2.cucumber.utils.TestUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import jakarta.inject.Inject;
import jakarta.inject.Named;

public class OrganisationUnit {

    @Inject
    @Named("testClient")
    private Dhis2HttpClient dhis2HttpClient;

    @Inject
    @Named("testUniqueId")
    private TestUniqueId testUniqueId;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationUnit.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static String level1Id;
    private final String level1Name;
    private final String openingDate;
    private final ArrayList<String> levelNamePrefix = new ArrayList<>();
    private final ArrayList<Map<String, String>> orgUnits = new ArrayList<Map<String, String>>();

    public static String getLevel1Id() throws Exception {
        return level1Id;
    }

    public OrganisationUnit(String rootOrganisationUnitName, String level2NamePrefix, String level3NamePrefix, String level4NamePrefix, String level5NamePrefix, String openingDate) throws Exception {
        this.level1Name = rootOrganisationUnitName;
        this.openingDate = openingDate;
        levelNamePrefix.add(level1Name);
        levelNamePrefix.add(level2NamePrefix);
        levelNamePrefix.add(level3NamePrefix);
        levelNamePrefix.add(level4NamePrefix);
        levelNamePrefix.add(level5NamePrefix);
    }

    public Map<String, String> createOrganisationUnit(int level) throws Exception {
        return createOrganisationUnitHierarchy(level);
    }

    private JsonNode getTestRoot() throws Exception {
        String response = dhis2HttpClient.doGet("api/organisationUnits?fields=id,name,shortName,openingDate,level&filter=level:eq:1&filter=name:eq:" + level1Name);
        LOGGER.info("Response {}", response);
        return MAPPER.readTree(response).get("organisationUnits");
    }

    private String createRoot() throws Exception {
        Map<String, String> orgUnit = new HashMap<>();
        if (getTestRoot().isEmpty()) {
            String id = dhis2HttpClient.getUniqueDhis2Id();
            orgUnit.put("id", id);
            orgUnit.put("name", this.level1Name);
            orgUnit.put("shortName", this.level1Name);
            orgUnit.put("openingDate", toISODateTimeString(this.openingDate));
            orgUnit.put("level", "1");
            level1Id = create(orgUnit);
            this.orgUnits.add(orgUnit);
            LOGGER.info("Root Organisation unit: {}", orgUnit);
        } else {
            orgUnit = MAPPER.convertValue(getTestRoot().get(0), HashMap.class);
            orgUnits.add(orgUnit);
            LOGGER.info("Response {}", orgUnit);
            level1Id = orgUnit.get("id");
        }
        return level1Id;
    }

    private Map<String, String> createOrganisationUnitHierarchy(int level) throws Exception {
        String parentId = createRoot();
        for (int i = 2; i <= level; i++) {
            String id = dhis2HttpClient.getUniqueDhis2Id();
            String name = this.levelNamePrefix.get(i - 1) + testUniqueId.hashCode();
            Map<String, String> orgUnit = new HashMap<>();
            orgUnit.put("id", id);
            orgUnit.put("name", name);
            orgUnit.put("shortName", name);
            orgUnit.put("openingDate", toISODateTimeString(this.openingDate));
            orgUnit.put("level", String.valueOf(i));
            orgUnit.put("parentId", parentId);
            this.orgUnits.add(orgUnit);
            parentId = create(orgUnit);
        }
        return Map.of("organisationUnitId", orgUnits.get(level - 1).get("id"), "organisationUnitName", orgUnits.get(level - 1).get("name"));
    }

    private String create(Map<String, String> orgUnit) throws Exception {
        Map<String, Object> organisationUnitTemplateContext = Map.of(
                "data", this,
                "orgUnit", orgUnit);
        String response = dhis2HttpClient.doPost(
                "api/organisationUnits",
                "create_organisation_unit.tpl.json",
                organisationUnitTemplateContext);
        LOGGER.info("Response {}", response);
        return MAPPER.readTree(response).get("response").get("uid").asText();
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
    }
}
