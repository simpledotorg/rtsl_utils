package org.rtsl.openmetrics.config;

import java.util.List;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.wrappers.IMetricProviderNameAware;
import org.rtsl.openmetrics.utils.wrappers.IMetricProviderWrapper;

public class MetricProviderDecorator {

    private final List<Class<IMetricProviderWrapper>> wrapperClasses;

    public MetricProviderDecorator(List<Class<IMetricProviderWrapper>> wrapperClasses) {
        this.wrapperClasses = wrapperClasses;
    }

    public MetricProvider decorateMetricProvider(MetricProvider metricProvider, MetricMetadata metadata) throws Exception {

        MetricProvider currentMetricProvider = metricProvider;
        for (Class<IMetricProviderWrapper> currentWrapperClass : wrapperClasses) {
            IMetricProviderWrapper currentWrapper = currentWrapperClass.getConstructor().newInstance();
            if (currentWrapper instanceof IMetricProviderNameAware && metadata != null) {
                IMetricProviderNameAware currentNameAwareWrapper = (IMetricProviderNameAware) currentWrapper;
                currentNameAwareWrapper.setMetricProviderName(metadata.getName());
                //TODO : log
            }
            currentWrapper.setMetricProvider(currentMetricProvider);
            currentMetricProvider = currentWrapper;

        }
        return currentMetricProvider;
    }

}
