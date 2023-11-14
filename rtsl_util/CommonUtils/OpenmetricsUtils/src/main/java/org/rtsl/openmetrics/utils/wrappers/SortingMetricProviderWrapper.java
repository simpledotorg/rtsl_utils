package org.rtsl.openmetrics.utils.wrappers;

import java.util.Collections;
import java.util.List;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;

public class SortingMetricProviderWrapper implements MetricProvider, IMetricProviderWrapper {

    private MetricProvider wrappedMetricProvider;

    @Override
    public void setMetricProvider(MetricProvider wrappedMetricProvider) {
        this.wrappedMetricProvider = wrappedMetricProvider;
    }

    @Override
    public List<Metric> getMetrics() {
        List<Metric> returnMetrics = wrappedMetricProvider.getMetrics();
        Collections.sort(returnMetrics);
        return returnMetrics;
    }

}
