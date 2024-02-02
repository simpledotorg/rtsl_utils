package org.rtsl.openmetrics.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.asynch.MetricCachingConsumer;
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
            if (currentMetadata.getAsynch()) {// MetricProvider is configured as asynch. Lets use some cache !
                MetricMetadata cacheMetaData = currentMetadata.clone();
                cacheMetaData.setAsynch(false);
                if (cacheMetaData.getType() != null) {
                    cacheMetaData.setType(cacheMetaData.getType() + "_cache");
                }
                MetricCachingConsumer cache = new MetricCachingConsumer(decoratedProvider, null);
                MetricProvider decoratedCache = decorator.decorateMetricProvider(cache, cacheMetaData);
                returnList.add(decoratedCache);
            } else {
                returnList.add(decoratedProvider);
            }
        }
        return returnList;
    }

}
