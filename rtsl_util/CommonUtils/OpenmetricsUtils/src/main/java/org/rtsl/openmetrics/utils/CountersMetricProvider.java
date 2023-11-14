package org.rtsl.openmetrics.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class CountersMetricProvider implements MetricProvider<AtomicLong> {

    private final Map<String, String> sharedLabels;
    private ConcurrentHashMap<String, Metric<AtomicLong>> metricsReference = new ConcurrentHashMap<>();
    private List<Metric<AtomicLong>> metrics = new ArrayList<>();

    public CountersMetricProvider() {
        this(new HashMap<>());
    }

    public CountersMetricProvider(Map<String, String> sharedLabels) {
        this.sharedLabels = sharedLabels;
    }

    @Override
    public List<Metric<AtomicLong>> getMetrics() {
        return metrics;
    }

    public Metric<AtomicLong> getMetric(String key, Map<String, String> labels) {
        String currentKey = StandardMetric.getStandardMetricFullName(key, labels);
        if (metricsReference.get(currentKey) == null) {
            return getMetricSafe(key, labels);
        }
        return metricsReference.get(currentKey);
    }

    private synchronized Metric<AtomicLong> getMetricSafe(String key, Map<String, String> labels) {
        // not efficient. Lets try to find better. but this works. for now.
        String currentKey = StandardMetric.getStandardMetricFullName(key, labels);
        if (metricsReference.get(currentKey) == null) {
            Map<String, String> allLabels = new HashMap<>(sharedLabels);
            allLabels.putAll(labels);
            StandardMetric<AtomicLong> newMetric = new StandardMetric(key, allLabels);
            newMetric.setMetricValue(new AtomicLong());
            metricsReference.put(currentKey, newMetric);
            metrics.add(newMetric);
        }
        return metricsReference.get(currentKey);

    }

}
