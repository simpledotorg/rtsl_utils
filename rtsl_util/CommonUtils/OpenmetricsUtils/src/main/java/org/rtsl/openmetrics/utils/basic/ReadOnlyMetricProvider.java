package org.rtsl.openmetrics.utils.basic;

import org.rtsl.openmetrics.utils.basic.ReadOnlyMetric;
import java.util.ArrayList;
import java.util.List;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;

public final class ReadOnlyMetricProvider implements MetricProvider {

    private final List<Metric> publicMetrics = new ArrayList<>();

    public ReadOnlyMetricProvider() {
    }

    public ReadOnlyMetricProvider(List<Metric> metrics) {
        addAll(metrics);
    }

    public void addAll(List<Metric> metrics) {
        for (Metric currentMetric : metrics) {
            add(currentMetric);
        }
    }

    public void add(Metric newMetric) {
        publicMetrics.add(new ReadOnlyMetric(newMetric));
    }

    public void clear() {
        publicMetrics.clear();
    }

    @Override
    public List<Metric> getMetrics() {
        return publicMetrics;
    }

}
