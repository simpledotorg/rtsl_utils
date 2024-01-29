package org.rtsl.openmetrics.utils.basic;

import java.util.ArrayList;
import java.util.List;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;

public final class AggregatingMetricProvider implements MetricProvider {

    private final List<MetricProvider> providers;

    public AggregatingMetricProvider(List<MetricProvider> providers) {
        this.providers = providers;
    }

    @Override
    public List<Metric> getMetrics() throws Exception {
        ArrayList<Metric> returnMetrics = new ArrayList<>();
        for (MetricProvider currentProvider : providers) {
            returnMetrics.addAll(currentProvider.getMetrics());
        }
        return returnMetrics;
    }

}
