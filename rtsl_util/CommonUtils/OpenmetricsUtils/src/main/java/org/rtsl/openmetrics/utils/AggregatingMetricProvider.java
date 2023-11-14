package org.rtsl.openmetrics.utils;

import java.util.ArrayList;
import java.util.List;

public final class AggregatingMetricProvider implements MetricProvider {

    private final List<MetricProvider> providers;

    public AggregatingMetricProvider(List<MetricProvider> providers) {
        this.providers = providers;
    }

    @Override
    public List<Metric> getMetrics() {
        ArrayList<Metric> returnMetrics = new ArrayList<>();
        for (MetricProvider currentProvider : providers) {
            returnMetrics.addAll(currentProvider.getMetrics());
        }
        return returnMetrics;
    }

}
