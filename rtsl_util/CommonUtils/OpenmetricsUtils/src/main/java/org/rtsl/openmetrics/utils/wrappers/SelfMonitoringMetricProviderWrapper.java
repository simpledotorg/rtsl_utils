package org.rtsl.openmetrics.utils.wrappers;

import java.util.ArrayList;
import java.util.List;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.StandardMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelfMonitoringMetricProviderWrapper implements IMetricProviderWrapper, IMetricProviderNameAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelfMonitoringMetricProviderWrapper.class);
    private String metric_prefix = "metric_collection_metadata";
    private String metricProviderName = "unknown";

    private MetricProvider wrappedMetricProvider;

    @Override
    public void setMetricProvider(MetricProvider wrappedMetricProvider) {
        this.wrappedMetricProvider = wrappedMetricProvider;
    }

    @Override
    public void setMetricProviderName(String metricProviderName) {
        this.metricProviderName = metricProviderName;
    }

    @Override
    public List<Metric> getMetrics() {
        LOGGER.debug("Wrapping Metric Provider <{}> to add performance metrics", metricProviderName);
        List<Metric> returnList = new ArrayList<>();
        long t1 = java.lang.System.currentTimeMillis();
        int error_count = 0;
        List<Metric> wrappedList;
        try {
            wrappedList = wrappedMetricProvider.getMetrics();
        } catch (Exception ex) { // TODO : log
            wrappedList = new ArrayList<>();
            error_count = 1;
        }
        long t2 = java.lang.System.currentTimeMillis();

        // TODO : replace by an AggregatingMetricProvider
        returnList.addAll(wrappedList); // horrible in term of perfs. Should be improved at some point

        returnList.add(new StandardMetric(metric_prefix + "_generation_time", t1, "monitoring_source", metricProviderName)); // horrible in term of perfs. Should be improved at some point
        returnList.add(new StandardMetric(metric_prefix + "_duration_milliseconds", t2 - t1, "monitoring_source", metricProviderName)); // horrible in term of perfs. Should be improved at some point
        returnList.add(new StandardMetric(metric_prefix + "_metrics_count", wrappedList.size(), "monitoring_source", metricProviderName)); // horrible in term of perfs. Should be improved at some point
        returnList.add(new StandardMetric(metric_prefix + "_error_status", error_count, "monitoring_source", metricProviderName)); // horrible in term of perfs. Should be improved at some point

        return returnList;
    }

}
