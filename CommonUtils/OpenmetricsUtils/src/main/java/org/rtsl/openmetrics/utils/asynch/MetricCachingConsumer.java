package org.rtsl.openmetrics.utils.asynch;

import java.util.List;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;

public class MetricCachingConsumer implements Runnable, MetricProvider {

    private final IMetricCache metricCache;
    private final MetricProvider asynchProvider;

    public MetricCachingConsumer(MetricProvider asynchProvider, IMetricCache metricCache) {
        this.metricCache = metricCache;
        this.asynchProvider = asynchProvider;
    }

    @Override
    public void run() {
        try {
            List<Metric> metrics = asynchProvider.getMetrics();
            metricCache.cacheMetrics(metrics);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public List getMetrics() throws Exception {
        return metricCache.getMetrics();
    }

}
