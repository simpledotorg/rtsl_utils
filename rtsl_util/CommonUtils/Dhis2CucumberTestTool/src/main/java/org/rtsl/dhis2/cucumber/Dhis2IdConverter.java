package org.rtsl.dhis2.cucumber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Dhis2IdConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2IdConverter.class);
    private static final String GET_TEI_ATTRIBUTES_URL = "api/trackedEntityAttributes?paging=false&fields=shortName,name,id";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Dhis2HttpClient dhis2HttpClient;

    private final Map<String, String> teiAttributeIdFromShortName = new HashMap<>();
    private final Map<String, String> teiAttributeIdFromName = new HashMap<>();
    private final Map<String, String> teiAttributeIdFromId = new HashMap<>();

    public Dhis2IdConverter(Dhis2HttpClient dhis2HttpClient) throws Exception {
        this.dhis2HttpClient = dhis2HttpClient;
    }

    private void getTeiAttributes() throws Exception {
        String response = dhis2HttpClient.doGet(GET_TEI_ATTRIBUTES_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("trackedEntityAttributes");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText();
                String currentShortName = jsonNode.get("shortName").asText();
                LOGGER.info("creating helper for trackedEntityAttribute {}:{}:{} ", currentId, currentName, currentShortName);
                teiAttributeIdFromShortName.put(currentShortName, currentId);
                teiAttributeIdFromName.put(currentName, currentId);
                teiAttributeIdFromId.put(currentId, currentId);

            }
        }
    }

    public String getTeiAttributeId(String candidateString) {
        if (teiAttributeIdFromId.get(candidateString) != null) {
            LOGGER.debug("String <{}> is already a trackedEntityAttribute id. Using it as it is.", candidateString);
            return candidateString;
        }
        String returnId = teiAttributeIdFromName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the trackedEntityAttribute name for id <{}>", candidateString, returnId);
            return returnId;
        }
        returnId = teiAttributeIdFromShortName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the trackedEntityAttribute shortName for id <{}>", candidateString, returnId);
            return returnId;
        }
        LOGGER.debug("String <{}> is not known as either an Id, Name or ShortName for trackedEntityAttribute. This is likely going to create a problem", candidateString);
        return candidateString;
    }

    public Map<String, String> convertTeiAttributes(Map<String, String> inputData) {
        Map<String, String> returnMap = new HashMap<>();
        for (String currentKey : inputData.keySet()) {
            returnMap.put(getTeiAttributeId(currentKey), inputData.get(currentKey));
        }
        return returnMap;
    }

}
