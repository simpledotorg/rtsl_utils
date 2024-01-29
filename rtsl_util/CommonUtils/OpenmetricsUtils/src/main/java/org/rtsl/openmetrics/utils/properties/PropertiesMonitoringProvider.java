package org.rtsl.openmetrics.utils.properties;

import org.rtsl.openmetrics.utils.basic.ReadOnlyMetric;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.StandardMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PropertiesMonitoringProvider implements MetricProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesMonitoringProvider.class);
    private static final String PREFIX = "__monitored__.";

    private final List<Metric> metrics = new ArrayList<>();
    private static final String ERROR_COUNT_METRIC_NAME = "properties_monitoring_error_count";
    private static final String SUCCESS_COUNT_METRIC_NAME = "properties_monitoring_metrics_count";

    public PropertiesMonitoringProvider(Properties referenceProperties, Map<String, String> additionnalLabels) {
        this(referenceProperties, additionnalLabels, "todo_");
    }

    public PropertiesMonitoringProvider(Properties referenceProperties) {
        this(referenceProperties, new HashMap<>());
    }

    public PropertiesMonitoringProvider(Properties referenceProperties, Map<String, String> additionnalLabels,
            String metricPrefix) {
        Integer errorCount = 0;
        Integer successCount = 0;
        for (Object currenyKey : referenceProperties.keySet()) {
            if (currenyKey.toString().startsWith(PREFIX)) {
                Map<String, String> labels = new HashMap<>();
                String currentPromKey = currenyKey.toString().substring(PREFIX.length());
                String currentPropertyValue = null;
                try {
                    String currentPropertyName = referenceProperties.getProperty(currenyKey.toString());
                    currentPropertyValue = referenceProperties.getProperty(currentPropertyName);
                    LOGGER.info("identified value to be monitored <{}:{}>", currentPromKey, currentPropertyValue);
                    labels.put("original_property_name", currentPropertyName);
                    labels.putAll(additionnalLabels);
                    if (!ERROR_COUNT_METRIC_NAME.equals(currentPromKey) && !SUCCESS_COUNT_METRIC_NAME.equals(currentPromKey)) {

                        StandardMetric currentMetric = new StandardMetric(metricPrefix + "property_" + currentPromKey, labels);
                        currentMetric.setMetricValue(new BigDecimal(currentPropertyValue));
                        metrics.add(new ReadOnlyMetric(currentMetric));
                        successCount++;
                    } else {
                        LOGGER.warn("Invalid property name: <{}>", currentPromKey);
                        errorCount++;
                    }
                } catch (Exception ex) {
                    LOGGER.warn(
                            String.format("Impossible to create Metric for name: <%s>, labels <%s> labels and value <%s>",
                                    currentPromKey, labels, currentPropertyValue), ex);
                    errorCount++;
                }
            }
        }
        // error
        StandardMetric errorMetric = new StandardMetric(metricPrefix + ERROR_COUNT_METRIC_NAME, additionnalLabels);
        errorMetric.setMetricValue(errorCount);
        metrics.add(new ReadOnlyMetric(errorMetric));
        // success
        StandardMetric successMetric = new StandardMetric(metricPrefix + SUCCESS_COUNT_METRIC_NAME, additionnalLabels);
        successMetric.setMetricValue(successCount);
        metrics.add(new ReadOnlyMetric(successMetric));
    }

    @Override
    public List<Metric> getMetrics() {
        return metrics;
    }

}
