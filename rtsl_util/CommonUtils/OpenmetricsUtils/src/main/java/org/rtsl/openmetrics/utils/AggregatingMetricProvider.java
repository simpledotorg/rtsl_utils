package org.rtsl.openmetrics.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AggregatingMetricProvider implements MetricProvider {

    private final List<MetricProvider> providers;
    private final boolean sort;

    public AggregatingMetricProvider(List<MetricProvider> providers, boolean sort) {
        this.providers = providers;
        this.sort = sort;
    }

    public AggregatingMetricProvider(List<MetricProvider> providers) {
        this(providers, true);
    }

    @Override
    public List<Metric> getMetrics() {
        ArrayList<Metric> returnMetrics = new ArrayList<>();
        for (MetricProvider currentProvider : providers) {
            returnMetrics.addAll(currentProvider.getMetrics());
        }
        if (sort) {
            Collections.sort(returnMetrics);
        }
        return returnMetrics;
    }

}
