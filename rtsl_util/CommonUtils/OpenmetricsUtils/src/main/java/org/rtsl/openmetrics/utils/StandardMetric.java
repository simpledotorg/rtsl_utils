package org.rtsl.openmetrics.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class StandardMetric<T extends Number> implements Metric<T> {

    public static final String METRIC_NAME_REGEXP = "^[a-zA-Z_][a-zA-Z0-9_]*$";
    public static final String METRIC_LABEL_KEY_REGEXP = "^[a-zA-Z_][a-zA-Z0-9_]*$";
    public static final String METRIC_LABEL_VALUE_REGEXP = ".*";

    // patterns
    private static final Pattern METRIC_NAME_PATTERN = Pattern.compile(METRIC_NAME_REGEXP);
    private static final Pattern METRIC_LABEL_KEY_PATTERN = Pattern.compile(METRIC_LABEL_KEY_REGEXP);
    private static final Pattern METRIC_LABEL_VALUE_PATTERN = Pattern.compile(METRIC_LABEL_VALUE_REGEXP, Pattern.DOTALL | Pattern.MULTILINE);

    // internal variables
    private final String metricFullName;

    private T metricValue;

    public StandardMetric(String metricName, Map<String, String> labels, T metricValue) {
        this(metricName, labels);
        setMetricValue(metricValue);
    }

    public StandardMetric(String metricName, Map<String, String> labels) throws IllegalArgumentException {
        this.metricFullName = getStandardMetricFullName(metricName, labels);
    }

    public StandardMetric(String metricName, String... labelsAndValues) throws IllegalArgumentException {
        this.metricFullName = getStandardMetricFullName(metricName, toMap(labelsAndValues));
    }

    public StandardMetric(String metricName, T value, String... labelsAndValues) throws IllegalArgumentException {
        this.metricFullName = getStandardMetricFullName(metricName, toMap(labelsAndValues));
        setMetricValue(value);
    }

    private static Map<String, String> toMap(String... keysAndValues) {
        if (keysAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("Input must be an even number of strings");
        }

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            String key = keysAndValues[i];
            String value = keysAndValues[i + 1];
            map.put(key, value);
        }

        return map;
    }

    public void setMetricValue(T metricValue) {
        this.metricValue = metricValue;
    }

    @Override
    public String getMetricFullName() {
        return metricFullName;
    }

    @Override
    public T getMetricValue() {
        return metricValue;
    }

    public static String getStandardMetricFullName(String metricName, Map<String, String> labels)
            throws IllegalArgumentException {
        if (!METRIC_NAME_PATTERN.matcher(metricName).matches()) {
            throw new IllegalArgumentException(String.format("Invalid metric name <%s> does not match regexp <%s>",
                    metricName, METRIC_NAME_REGEXP));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(metricName);
        boolean isFirst = true;
        sb.append("{");
        for (String currentLabelKey : labels.keySet()) {
            if (!METRIC_LABEL_KEY_PATTERN.matcher(currentLabelKey).matches()) {
                throw new IllegalArgumentException(String.format("Invalid label name <%s> does not match regexp <%s>",
                        currentLabelKey, METRIC_LABEL_KEY_REGEXP));
            }
            String currentLabelValue = labels.get(currentLabelKey);
            if (currentLabelValue != null) {
                if (!METRIC_LABEL_VALUE_PATTERN.matcher(currentLabelValue).matches()) {
                    throw new IllegalArgumentException(String.format("Invalid label value <%s> does not match regexp <%s>",
                            currentLabelValue, METRIC_LABEL_VALUE_PATTERN));
                }
                if (!isFirst) {
                    sb.append(",");
                }
                isFirst = false;
                sb.append("")
                        .append(currentLabelKey)
                        .append("=\"")
                        .append(standardizeLabelValue(currentLabelValue))
                        .append("\"");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private static String standardizeLabelValue(String rawValue) {
        if (rawValue == null) {
            return null;
        }
        return rawValue.replace("\"", "\\\"").replace("\n", "\\n");
    }

}
