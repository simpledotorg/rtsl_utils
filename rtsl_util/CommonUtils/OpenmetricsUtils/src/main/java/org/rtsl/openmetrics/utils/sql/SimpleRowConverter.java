package org.rtsl.openmetrics.utils.sql;

import java.util.HashMap;
import java.util.Map;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.StandardMetric;

public class SimpleRowConverter implements RowConverter {

    private String metricName;
    private String metricValueKey;
    private Map<String, String> labelsKeys;

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricValueKey() {
        return metricValueKey;
    }

    public void setMetricValueKey(String metricValueKey) {
        this.metricValueKey = metricValueKey;
    }

    public Map<String, String> getLabelsKeys() {
        return labelsKeys;
    }

    public void setLabelsKeys(Map<String, String> labelsKeys) {
        this.labelsKeys = labelsKeys;
    }

    @Override
    public Metric getMetric(Map<String, Object> row) {
        Map<String, String> labels = new HashMap<>();
        String name = metricName;

        // get the labels
        for (String currentLabelName : labelsKeys.keySet()) {
            String currentLabelKey = labelsKeys.get(currentLabelName);
            Object currentLabelValue = row.get(currentLabelKey);
            String currentObjectValueString = currentLabelValue == null ? "" : currentLabelValue.toString();
            labels.put(currentLabelName, currentObjectValueString);
        }

        // creates the object
        StandardMetric metric = new StandardMetric(name, labels);

        // get the value
        Object valueObject = row.get(metricValueKey);
        if (valueObject instanceof Number) {
            metric.setMetricValue((Number) valueObject);
        } else {
            // TODO : log warning
        }

        return metric;

    }

}
