package org.rtsl.dhis2.cucumber.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class Dhis2IdConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2IdConverter.class);
    private static final String GET_TEI_ATTRIBUTES_URL = "api/trackedEntityAttributes?paging=false&fields=shortName,name,id";
    private static final String GET_PROGRAM_INDICATOR_URL = "api/programIndicators?paging=false&fields=shortName,name,id";
    private static final String GET_INDICATOR_URL = "api/indicators?paging=false&fields=shortName,name,id";
    private static final String GET_PROGRAM_URL = "api/programs?paging=false&fields=shortName,name,id";
    private static final String GET_DATA_ELEMENT_URL = "api/dataElements?paging=false&fields=shortName,name,id";
    private static final String GET_JOB_CONFIGURATION_URL = "api/jobConfigurations?fields=name,id";
    private static final String GET_PROGRAM_STAGE_URL = "api/programStages?paging=false&fields=name,id";



    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Dhis2HttpClient dhis2HttpClient;
    // TODO Refactor the code. We probably do not need all these maps.
    private final Map<String, String> trackedEntityAttributeIdFromShortName = new HashMap<>();
    private final Map<String, String> trackedEntityAttributeIdFromName = new HashMap<>();
    private final Map<String, String> trackedEntityAttributeIdFromId = new HashMap<>();
    private final Map<String, String> programIndicatorIdFromShortName = new HashMap<>();
    private final Map<String, String> programIndicatorIdFromName = new HashMap<>();
    private final Map<String, String> programIndicatorIdFromId = new HashMap<>();
    private final Map<String, String> jobConfigurationIdFromName = new HashMap<>();
    private final Map<String, String> jobConfigurationIdFromId = new HashMap<>();
    private final Map<String, String> programIdFromShortName = new HashMap<>();
    private final Map<String, String> programIdFromName = new HashMap<>();
    private final Map<String, String> programIdFromId = new HashMap<>();
    private final Map<String, String> dataElementIdFromShortName = new HashMap<>();
    private final Map<String, String> dataElementIdFromName = new HashMap<>();
    private final Map<String, String> dataElementIdFromId = new HashMap<>();

    private final Map<String, String> programStageIdFromName = new HashMap<>();
    private final Map<String, String> programStageIdFromId = new HashMap<>();
    private final Map<String, String> indicatorIdFromShortName = new HashMap<>();
    private final Map<String, String> indicatorIdFromName = new HashMap<>();
    private final Map<String, String> indicatorIdFromId = new HashMap<>();

    public Dhis2IdConverter(Dhis2HttpClient dhis2HttpClient) throws Exception {
        this.dhis2HttpClient = dhis2HttpClient;
    }

    private void getMetadata() throws Exception {
        getTeiAttributes();
        getProgramIndicators();
        getJobConfigurations();
        getPrograms();
        getDataElements();
        getProgramStages();
    }

    private void getProgramStages() throws Exception {
        String response = dhis2HttpClient.doGet(GET_PROGRAM_STAGE_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("programStages");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText().trim();
                LOGGER.info("creating helper for programs {}:{}", currentId, currentName);
                programStageIdFromName.put(currentName, currentId);
                programStageIdFromId.put(currentId, currentId);
            }
        }
    }

    private void getDataElements() throws Exception {
        String response = dhis2HttpClient.doGet(GET_DATA_ELEMENT_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("dataElements");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText().trim();
                String currentShortName = jsonNode.get("shortName").asText().trim();
                LOGGER.info("creating helper for data elements {}:{}:{} ", currentId, currentName, currentShortName);
                dataElementIdFromShortName.put(currentShortName, currentId);
                dataElementIdFromName.put(currentName, currentId);
                dataElementIdFromId.put(currentId, currentId);

            }
        }
    }

    private void getPrograms() throws Exception {
        String response = dhis2HttpClient.doGet(GET_PROGRAM_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("programs");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText().trim();
                String currentShortName = jsonNode.get("shortName").asText().trim();
                LOGGER.info("creating helper for programs {}:{}:{} ", currentId, currentName, currentShortName);
                programIdFromShortName.put(currentShortName, currentId);
                programIdFromName.put(currentName, currentId);
                programIdFromId.put(currentId, currentId);

            }
        }
    }

    private void getTeiAttributes() throws Exception {
        String response = dhis2HttpClient.doGet(GET_TEI_ATTRIBUTES_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("trackedEntityAttributes");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText().trim();
                String currentShortName = jsonNode.get("shortName").asText().trim();
                LOGGER.info("creating helper for trackedEntityAttribute {}:{}:{} ", currentId, currentName, currentShortName);
                trackedEntityAttributeIdFromShortName.put(currentShortName, currentId);
                trackedEntityAttributeIdFromName.put(currentName, currentId);
                trackedEntityAttributeIdFromId.put(currentId, currentId);

            }
        }
    }

    private void getProgramIndicators() throws Exception {
        String response = dhis2HttpClient.doGet(GET_PROGRAM_INDICATOR_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("programIndicators");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText().trim();
                String currentShortName = jsonNode.get("shortName").asText().trim();
                LOGGER.info("creating helper for program indicators {}:{}:{} ", currentId, currentName, currentShortName);
                programIndicatorIdFromShortName.put(currentShortName, currentId);
                programIndicatorIdFromName.put(currentName, currentId);
                programIndicatorIdFromId.put(currentId, currentId);
            }
        }
    }

    private void getIndicators() throws Exception {
        String response = dhis2HttpClient.doGet(GET_INDICATOR_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("programIndicators");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText().trim();
                String currentShortName = jsonNode.get("shortName").asText().trim();
                LOGGER.info("creating helper for program indicators {}:{}:{} ", currentId, currentName, currentShortName);
                indicatorIdFromShortName.put(currentShortName, currentId);
                indicatorIdFromName.put(currentName, currentId);
                indicatorIdFromId.put(currentId, currentId);
            }
        }
    }

    private void getJobConfigurations() throws Exception {
        String response = dhis2HttpClient.doGet(GET_JOB_CONFIGURATION_URL);
        JsonNode arrayNode = objectMapper.readTree(response).get("jobConfigurations");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                String currentId = jsonNode.get("id").asText();
                String currentName = jsonNode.get("name").asText().trim();
                LOGGER.info("creating helper for job configurations {}:{} ", currentId, currentName);
                jobConfigurationIdFromName.put(currentName, currentId);
                jobConfigurationIdFromId.put(currentId, currentId);
            }
        }
    }

    public String getProgramIndicatorId(String candidateString) {
        if (programIndicatorIdFromId.get(candidateString) != null) {
            LOGGER.debug("String <{}> is already a metadata id. Using it as it is.", candidateString);
            return candidateString;
        }
        String returnId = programIndicatorIdFromName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata name for id <{}>", candidateString, returnId);
            return returnId;
        }
        returnId = programIndicatorIdFromShortName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata shortName for id <{}>", candidateString, returnId);
            return returnId;
        }
        LOGGER.debug("String <{}> is not known as either an Id, Name or ShortName for metadata. This is likely going to create a problem", candidateString);
        return candidateString;
    }

    public String getIndicatorId(String candidateString) {
        if (indicatorIdFromId.get(candidateString) != null) {
            LOGGER.debug("String <{}> is already a metadata id. Using it as it is.", candidateString);
            return candidateString;
        }
        String returnId = indicatorIdFromName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata name for id <{}>", candidateString, returnId);
            return returnId;
        }
        returnId = indicatorIdFromShortName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata shortName for id <{}>", candidateString, returnId);
            return returnId;
        }
        LOGGER.debug("String <{}> is not known as either an Id, Name or ShortName for metadata. This is likely going to create a problem", candidateString);
        return candidateString;
    }

    public String getTrackedEntityAttributeId(String candidateString) {
        if (trackedEntityAttributeIdFromId.get(candidateString) != null) {
            LOGGER.debug("String <{}> is already a metadata id. Using it as it is.", candidateString);
            return candidateString;
        }
        String returnId = trackedEntityAttributeIdFromName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata name for id <{}>", candidateString, returnId);
            return returnId;
        }
        returnId = trackedEntityAttributeIdFromShortName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata shortName for id <{}>", candidateString, returnId);
            return returnId;
        }
        LOGGER.debug("String <{}> is not known as either an Id, Name or ShortName for metadata. This is likely going to create a problem", candidateString);
        return candidateString;
    }

    public String getDataElementId(String candidateString) {
        if (dataElementIdFromId.get(candidateString) != null) {
            LOGGER.debug("String <{}> is already a metadata id. Using it as it is.", candidateString);
            return candidateString;
        }
        String returnId = dataElementIdFromName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata name for id <{}>", candidateString, returnId);
            return returnId;
        }
        returnId = dataElementIdFromShortName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata shortName for id <{}>", candidateString, returnId);
            return returnId;
        }
        LOGGER.debug("String <{}> is not known as either an Id, Name or ShortName for metadata. This is likely going to create a problem", candidateString);
        return candidateString;
    }

    public String getJobConfigurationId(String candidateString) {
        if (jobConfigurationIdFromId.get(candidateString) != null) {
            LOGGER.debug("String <{}> is already a metadata id. Using it as it is.", candidateString);
            return candidateString;
        }
        String returnId = jobConfigurationIdFromName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata name for id <{}>", candidateString, returnId);
            return returnId;
        }
        LOGGER.debug("String <{}> is not known as either an Id, Name or ShortName for metadata. This is likely going to create a problem", candidateString);
        return candidateString;
    }

    public String getProgramId(String candidateString) {
        if (programIdFromId.get(candidateString) != null) {
            LOGGER.debug("String <{}> is already a metadata id. Using it as it is.", candidateString);
            return candidateString;
        }
        String returnId = programIdFromName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata name for id <{}>", candidateString, returnId);
            return returnId;
        }
        returnId = programIdFromShortName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata name for id <{}>", candidateString, returnId);
            return returnId;
        }
        LOGGER.debug("String <{}> is not known as either an Id, Name or ShortName for metadata. This is likely going to create a problem", candidateString);
        return candidateString;
    }

    public String getProgramStageId(String candidateString) {
        if (programStageIdFromId.get(candidateString) != null) {
            LOGGER.debug("String <{}> is already a metadata id. Using it as it is.", candidateString);
            return candidateString;
        }
        String returnId = programStageIdFromName.get(candidateString);
        if (returnId != null) {
            LOGGER.debug("String <{}> is the metadata name for id <{}>", candidateString, returnId);
            return returnId;
        }
        LOGGER.debug("String <{}> is not known as either an Id, Name or ShortName for metadata. This is likely going to create a problem", candidateString);
        return candidateString;
    }

    public Map<String, String> convertMetadata(Map<String, String> inputData, String metadata) {
        Map<String, String> returnMap = new HashMap<>();

        switch (metadata){
            case "trackedEntityAttribute":
                for (String currentKey : inputData.keySet()) {
                    returnMap.put(getTrackedEntityAttributeId(currentKey), inputData.get(currentKey));
                }
                break;
            case "dataElement":
                for (String currentKey : inputData.keySet()) {
                    returnMap.put(getDataElementId(currentKey), inputData.get(currentKey));
                }
                break;
            case "programIndicator":
                for (String currentKey : inputData.keySet()) {
                    returnMap.put(getProgramIndicatorId(currentKey), inputData.get(currentKey));
                }
                break;
            case "jobConfiguration":
                for (String currentKey : inputData.keySet()) {
                    returnMap.put(getJobConfigurationId(currentKey), inputData.get(currentKey));
                }
                break;
            case "program":
                for (String currentKey : inputData.keySet()) {
                    returnMap.put(getProgramId(currentKey), inputData.get(currentKey));
                }
                break;
            case "programStage":
                for (String currentKey : inputData.keySet()) {
                    returnMap.put(getProgramStageId(currentKey), inputData.get(currentKey));
                }
                break;
        }

        return returnMap;
    }
}
