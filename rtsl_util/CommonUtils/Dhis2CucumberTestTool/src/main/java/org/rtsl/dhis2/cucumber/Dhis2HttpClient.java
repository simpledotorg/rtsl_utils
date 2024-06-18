package org.rtsl.dhis2.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dhis2HttpClient {
    // TODO factorize code, methods are similar enough to allow this.

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2HttpClient.class);

    private final String dhis2RootUrl;
    private final String dhis2Username;
    private final String dhis2Password;
    private final String basicAuthString;

    Configuration freemarkerConfig;

    public Dhis2HttpClient(String dhis2RootUrl, String dhis2Username, String dhis2Password) {
        this.dhis2RootUrl = dhis2RootUrl;
        this.dhis2Username = dhis2Username;
        this.dhis2Password = dhis2Password;
        this.basicAuthString = "Basic " + Base64.getEncoder().encodeToString((dhis2Username + ":" + dhis2Password).getBytes());
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_33);
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates/");
    }

    public String doPut(String relativeUrl, String templateName, Object templateContext) throws Exception {
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

}
