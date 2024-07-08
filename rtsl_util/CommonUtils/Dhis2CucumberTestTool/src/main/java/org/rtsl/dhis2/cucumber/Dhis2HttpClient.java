package org.rtsl.dhis2.cucumber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dhis2HttpClient {
    // TODO factorize code, methods are similar enough to allow this.

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2HttpClient.class);

    private final String dhis2RootUrl;
    private final String basicAuthString;

    Configuration freemarkerConfig;

    public Dhis2HttpClient(String dhis2RootUrl, String dhis2Username, String dhis2Password) {
        this.dhis2RootUrl = dhis2RootUrl;
        LOGGER.info("Creating new HTTP Client for URL: {}", this.dhis2RootUrl);
        this.basicAuthString = "Basic " + Base64.getEncoder().encodeToString((dhis2Username + ":" + dhis2Password).getBytes());
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_33);
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates/");
    }

    public String doPutWithTemplate(String relativeUrl, String templateName, Object templateContext) throws Exception {
        LOGGER.info("Doing PUT call on url <{}> based on template <{}>", dhis2RootUrl + relativeUrl, templateName);
        // Gets the request Body
        Template actionTemplate = freemarkerConfig.getTemplate(templateName);
        StringWriter sw = new StringWriter();
        actionTemplate.process(templateContext, sw);
        String body = sw.toString();
        // Makes the call
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            HttpPut request = new HttpPut(dhis2RootUrl + relativeUrl);
            request.setEntity(new StringEntity(body));
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", basicAuthString);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // reads the answer
                StringBuilder sb = new StringBuilder();
                return new String(response.getEntity().getContent().readAllBytes());
            }

        }

    }
    
        public String doPutWithBody(String relativeUrl, String body) throws Exception {
        LOGGER.info("Doing PUT call on url <{}> with body <{}>", dhis2RootUrl + relativeUrl, body);
        // Makes the call
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            HttpPut request = new HttpPut(dhis2RootUrl + relativeUrl);
            request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json;charset=UTF-8");
            request.setHeader("Authorization", basicAuthString);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // reads the answer
                StringBuilder sb = new StringBuilder();
                return new String(response.getEntity().getContent().readAllBytes());
            }

        }

    }

    public String doPost(String relativeUrl, String templateName, Object templateContext) throws Exception { // TODO : factorize, possibly make a class ...
        LOGGER.info("Doing POST call on url <{}> based on template <{}>", dhis2RootUrl + relativeUrl, templateName);
        // Gets the request Body
        Template actionTemplate = freemarkerConfig.getTemplate(templateName);
        StringWriter sw = new StringWriter();
        actionTemplate.process(templateContext, sw);
        String body = sw.toString();
        // Makes the call
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            HttpPost request = new HttpPost(dhis2RootUrl + relativeUrl);
            request.setEntity(new StringEntity(body));
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", basicAuthString);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // reads the answer
                StringBuilder sb = new StringBuilder();
                return new String(response.getEntity().getContent().readAllBytes());
            }

        }

    }

    public String doPost(String relativeUrl) throws Exception{
        LOGGER.info("Doing POST call on url <{}>", dhis2RootUrl + relativeUrl);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            HttpPost request = new HttpPost(dhis2RootUrl + relativeUrl);
            request.setHeader("Authorization", basicAuthString);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // reads the answer
                StringBuilder sb = new StringBuilder();
                return new String(response.getEntity().getContent().readAllBytes());
            }

        }
    }

    public String doPatch(String relativeUrl, String templateName, Object templateContext) throws Exception { // TODO : factorize, possibly make a class ...
        LOGGER.debug("Doing PATCH call on url <{}> based on template <{}>", dhis2RootUrl + relativeUrl, templateName);
        // Gets the request Body
        Template actionTemplate = freemarkerConfig.getTemplate(templateName);
        StringWriter sw = new StringWriter();
        actionTemplate.process(templateContext, sw);
        String body = sw.toString();
        // Makes the call
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            HttpPatch request = new HttpPatch(dhis2RootUrl + relativeUrl);
            request.setEntity(new StringEntity(body));
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json-patch+json");
            request.setHeader("Authorization", basicAuthString);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // reads the answer
                StringBuilder sb = new StringBuilder();
                return new String(response.getEntity().getContent().readAllBytes());
            }

        }
    }

    public String doGet(String relativeUrl) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            HttpGet request = new HttpGet(dhis2RootUrl + relativeUrl);
            LOGGER.info("Making GET call to url: {}", dhis2RootUrl + relativeUrl);
            LOGGER.info("Making GET call to url: {}", request.getUri());
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", basicAuthString);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // reads the answer
                StringBuilder sb = new StringBuilder();
                return new String(response.getEntity().getContent().readAllBytes());
            }

        }
    }

    public String getCurrentUserId() throws Exception {
        String response = doGet("api/me?fields=id");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> parsedResponse = objectMapper.readValue(response, HashMap.class);
        return parsedResponse.get("id");
    }

    public String getGenerateUniqueId() throws Exception {
        String response = doGet("api/system/id?limit=1");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        ArrayNode ids = (ArrayNode) rootNode.get("codes");
        return ids.get(0).asText();

    }

}
