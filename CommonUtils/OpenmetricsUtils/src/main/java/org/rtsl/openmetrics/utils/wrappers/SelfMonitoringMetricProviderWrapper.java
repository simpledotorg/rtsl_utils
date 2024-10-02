package org.rtsl.openmetrics.utils.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.StandardMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SelfMonitoringMetricProviderWrapper implements IMetricProviderWrapper, IMetricProviderNameAware, IExtraLabelsAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelfMonitoringMetricProviderWrapper.class);
    private String metric_prefix = "metric_collection_metadata";
    private static final String DEFAULT_PROVIDER_NAME = "unknown";
    private final Map<String, String> all_labels = new HashMap<>();
    String metricProviderName = DEFAULT_PROVIDER_NAME;

    private MetricProvider wrappedMetricProvider;

    public SelfMonitoringMetricProviderWrapper() {
        setMetricProviderName(DEFAULT_PROVIDER_NAME);
    }

    @Override
    public void setMetricProvider(MetricProvider wrappedMetricProvider) {
        this.wrappedMetricProvider = wrappedMetricProvider;
    }

    @Override
    public void setMetricProviderName(String metricProviderName) {
        this.metricProviderName = metricProviderName;
        all_labels.put("monitoring_source", metricProviderName);
    }

    @Override
    public void setExtraLabels(Map<String, String> extraLabels) {
        all_labels.putAll(extraLabels);
    }

    public void setMetric_prefix(String metric_prefix) {
        this.metric_prefix = metric_prefix;
    }

    @Override
    public List<Metric> getMetrics() {
        LOGGER.debug("Wrapping Metric Provider <{}> with name <{}> to add performance metrics", wrappedMetricProvider, metricProviderName);
        List<Metric> returnList = new ArrayList<>();
        long t1 = java.lang.System.currentTimeMillis();
        int error_count = 0;
        List<Metric> wrappedList;
        try {
            wrappedList = wrappedMetricProvider.getMetrics();
        } catch (Exception ex) {
            LOGGER.warn("Exception occured while running getting metrics: ", ex);// TODO : log
            wrappedList = new ArrayList<>();
            error_count = 1;
        }
        long t2 = java.lang.System.currentTimeMillis();
        double durationInSecond = ((double) (t2 - t1)) / 1000;
        LOGGER.debug("Obtained <{}> metrics in <{}> milliseconds", wrappedList.size(), t2 - t1);

        // TODO : replace by an AggregatingMetricProvider
        returnList.addAll(wrappedList); // horrible in term of perfs. Should be improved at some point

        returnList.add(new StandardMetric(metric_prefix + "_generation_time", all_labels, t1)); // horrible in term of perfs. Should be improved at some point
        returnList.add(new StandardMetric(metric_prefix + "_duration_seconds", all_labels, durationInSecond)); // horrible in term of perfs. Should be improved at some point
        returnList.add(new StandardMetric(metric_prefix + "_metrics_count", all_labels, wrappedList.size())); // horrible in term of perfs. Should be improved at some point
        returnList.add(new StandardMetric(metric_prefix + "_error_status", all_labels, error_count)); // horrible in term of perfs. Should be improved at some point

        LOGGER.debug("Returning <{}> metrics after adding self monitoring info", returnList.size());
        return returnList;
    }

}
