package org.rtsl.openmetrics.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.basic.ParallelMetricProvider;

public class DecoratingParallelMetricProvider extends ParallelMetricProvider {

    public DecoratingParallelMetricProvider(Map<MetricMetadata, MetricProvider> providers, MetricProviderDecorator decorator, int processingPoolSize) throws Exception {
        super(getWrappedMetrics(providers, decorator), processingPoolSize);
    }

    private static List<MetricProvider> getWrappedMetrics(Map<MetricMetadata, MetricProvider> providers, MetricProviderDecorator decorator) throws Exception {
        List<MetricProvider> returnList = new ArrayList<>();
        for (MetricMetadata currentMetadata : providers.keySet()) {
            MetricProvider currentProvider = providers.get(currentMetadata);
            MetricProvider decoratedProvider = decorator.decorateMetricProvider(currentProvider, currentMetadata);
            returnList.add(decoratedProvider);
        }
        return returnList;
    }

}
