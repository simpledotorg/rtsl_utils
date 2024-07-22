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
    private static final String GET_PROGRAM_INDICATOR_URL = "api/programIndicators?paging=false&fields=shortName,name,id";
    private static final String GET_JOB_CONFIGURATION_URL = "api/jobConfigurations?fields=name,id";


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Dhis2HttpClient dhis2HttpClient;

    private final Map<String, String> idFromShortName = new HashMap<>();
    private final Map<String, String> idFromName = new HashMap<>();
    private final Map<String, String> idFromId = new HashMap<>();

    public Dhis2IdConverter(Dhis2HttpClient dhis2HttpClient) throws Exception {
        this.dhis2HttpClient = dhis2HttpClient;
    }

    private void getMetadata()throws Exception {
        getTeiAttributes();
        getProgramIndicators();
        getJobConfigurations();
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
                idFromShortName.put(currentShortName, currentId);
                idFromName.put(currentName, currentId);
                idFromId.put(currentId, currentId);

            }
        }
    }

    private void getProgramIndicators() throws Exception {
        String response = dhis2HttpClient.doGet(GET_PROGRAM_INDICATOR_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("programIndicators");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText();
                String currentShortName = jsonNode.get("shortName").asText();
                LOGGER.info("creating helper for trackedEntityAttribute {}:{}:{} ", currentId, currentName, currentShortName);
                idFromShortName.put(currentShortName, currentId);
                idFromName.put(currentName, currentId);
                idFromId.put(currentId, currentId);
            }
        }
    }
    private void getJobConfigurations() throws Exception {
        String response = dhis2HttpClient.doGet(GET_JOB_CONFIGURATION_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("jobConfigurations");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText();
                LOGGER.info("creating helper for trackedEntityAttribute {}:{} ", currentId, currentName);
                idFromName.put(currentName, currentId);
                idFromId.put(currentId, currentId);
            }
        }
    }

    public String getMetadataId(String candidateString) {
        if (idFromId.get(candidateString) != null) {
            LOGGER.debug("String <{}> is already a metadata id. Using it as it is.", candidateString);
            return candidateString;
        }
        String returnId = idFromName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata name for id <{}>", candidateString, returnId);
            return returnId;
        }
        returnId = idFromShortName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata shortName for id <{}>", candidateString, returnId);
            return returnId;
        }
        LOGGER.debug("String <{}> is not known as either an Id, Name or ShortName for metadata. This is likely going to create a problem", candidateString);
        return candidateString;
    }

    public Map<String, String> convertMetadata(Map<String, String> inputData) {
        Map<String, String> returnMap = new HashMap<>();
        for (String currentKey : inputData.keySet()) {
            returnMap.put(getMetadataId(currentKey), inputData.get(currentKey));
        }
        return returnMap;
    }

}
