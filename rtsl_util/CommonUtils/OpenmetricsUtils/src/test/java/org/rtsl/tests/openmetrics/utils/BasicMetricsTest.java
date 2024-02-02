package org.rtsl.tests.openmetrics.utils;

import org.rtsl.openmetrics.utils.basic.CountersMetricProvider;
import org.rtsl.openmetrics.utils.StandardMetric;
import org.rtsl.openmetrics.utils.servlet.OpenmetricsProducerServlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtsl.config.dynamic.folder.FolderCrawler;
import org.rtsl.openmetrics.config.MetricMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/test.context.xml")
public final class BasicMetricsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicMetricsTest.class);

    @Resource(name = "appProperties")
    Properties testProperties;

    @Resource(name = "metric1")
    StandardMetric metric1;

    @Resource(name = "metric2")
    StandardMetric metric2;

    @Resource(name = "countersMonitoringProvider")
    CountersMetricProvider countersMonitoringProvider;

    @Resource(name = "metricCrawler")
    FolderCrawler metricCrawler;

    private OpenmetricsProducerServlet servlet = new OpenmetricsProducerServlet();

    @Test
    public void test0() throws Exception {
        LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        LOGGER.info("" + testProperties);
        LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        LOGGER.info("" + metric1.getAsString());
        LOGGER.info("" + metric2.getAsString());
    }

    @Test
    public void testInvalidMetricName() throws Exception {
        IllegalArgumentException expectedException = null;
        try {
            StandardMetric invalidMetric = new StandardMetric("invalidMetric@Name", new HashMap<String, String>());
            LOGGER.info("This statement should not be reached", invalidMetric);
        } catch (IllegalArgumentException ex) {
            expectedException = ex;

        }
        Assert.assertNotNull(expectedException);
        LOGGER.info("rightfully triggered exception: {}", expectedException.getMessage());
    }

    @Test
    public void testInvalidLabelName() throws Exception {
        IllegalArgumentException expectedException = null;
        Map<String, String> labels = new HashMap<>();
        labels.put("invalidLabel@Name", "nope");
        labels.put("validLabelName", "yop");
        try {
            StandardMetric invalidMetric = new StandardMetric("validMetricName", labels);
            LOGGER.info("This statement should not be reached", invalidMetric);
        } catch (IllegalArgumentException ex) {
            expectedException = ex;

        }
        Assert.assertNotNull(expectedException);
        LOGGER.info("rightfully triggered exception: {}", expectedException.getMessage());
    }

    @Test
    public void testInvalidLabelValueQuote() throws Exception {
        IllegalArgumentException expectedException = null;
        Map<String, String> labels = new HashMap<>();
        LOGGER.info("Value regexp is <{}>", StandardMetric.METRIC_LABEL_VALUE_REGEXP);
        labels.put("valid_label_name", "invalid \" value \" ");
        labels.put("validLabelName", "yop");

        StandardMetric invalidMetric = new StandardMetric("validMetricName", labels);
        LOGGER.info("Generated Metric name with escaped quotes <{}>", invalidMetric.getMetricFullName());

    }

    @Test
    public void testInvalidLabelValueNewLine() throws Exception {
        IllegalArgumentException expectedException = null;
        Map<String, String> labels = new HashMap<>();
        LOGGER.info("Value regexp is <{}>", StandardMetric.METRIC_LABEL_VALUE_REGEXP);
        labels.put("valid_label_name", "invalid \n value \n ");
        labels.put("validLabelName", "yop");

        StandardMetric invalidMetric = new StandardMetric("validMetricName", labels);
        LOGGER.info("Generated metric name without carriage return <{}>", invalidMetric.getMetricFullName());

        int numberOfLines = invalidMetric.getMetricFullName().split("\r\n|\r|\n").length;
        Assert.assertEquals(1, numberOfLines);
    }

    @Test
    public void testServlet() throws ServletException, IOException {

        MockHttpServletResponse testResponse = new MockHttpServletResponse();
        servlet.doPost(null, testResponse);
        LOGGER.info("Ouput  is:\n{}", testResponse.getContentAsString());
        testResponse = new MockHttpServletResponse();
        servlet.doPost(null, testResponse);
        LOGGER.info("Ouput  is:\n{}", testResponse.getContentAsString());
        testResponse = new MockHttpServletResponse();
        servlet.doPost(null, testResponse);
        LOGGER.info("Ouput  is:\n{}", testResponse.getContentAsString());

    }

    @Test
    public void testCounters() throws ServletException, IOException {
        MockHttpServletResponse testResponse = new MockHttpServletResponse();
        Map<String, String> label1 = new HashMap<>();
        label1.put("flow", "flow1");
        label1.put("status", "success");
        Map<String, String> label2 = new HashMap<>();
        label2.put("flow", "flow1");
        label2.put("status", "error");
        AtomicLong al1 = countersMonitoringProvider.getMetric("testExecutionCounters", label1).getMetricValue();
        AtomicLong al2 = countersMonitoringProvider.getMetric("testExecutionCounters", label2).getMetricValue();

        testResponse = new MockHttpServletResponse();
        servlet.doPost(null, testResponse);
        LOGGER.info("testCounters Ouput  is:\n{}", testResponse.getContentAsString());

        al1.incrementAndGet();
        al1.incrementAndGet();
        al1.incrementAndGet();
        al1.incrementAndGet();
        al2.incrementAndGet();

        testResponse = new MockHttpServletResponse();
        servlet.doPost(null, testResponse);
        LOGGER.info("testCounters Ouput  is:\n{}", testResponse.getContentAsString());

        al1.incrementAndGet();
        al1.incrementAndGet();
        al1.incrementAndGet();
        al1.incrementAndGet();
        al2.incrementAndGet();

        testResponse = new MockHttpServletResponse();
        servlet.doPost(null, testResponse);
        LOGGER.info("testCounters Ouput  is:\n{}", testResponse.getContentAsString());

    }

    @Resource(name = "simpleConsumer")
    Runnable asynchFileWriterConsumer;

    @Test
    public void testAsynch() throws ServletException, IOException {
        asynchFileWriterConsumer.run();
    }

    @Test
    public void testCrawler() throws Exception {
        try {
            LOGGER.info("testing");
            Map<MetricMetadata, Object> parsedObjects = metricCrawler.getAll();
            LOGGER.info("___" + parsedObjects.toString());
        } catch (Exception ex) {
            LOGGER.warn("Exception occurred", ex);
            throw ex;
        }
    }

}
