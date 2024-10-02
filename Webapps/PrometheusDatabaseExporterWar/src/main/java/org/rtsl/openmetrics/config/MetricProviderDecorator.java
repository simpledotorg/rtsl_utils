package org.rtsl.openmetrics.config;

import java.util.List;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.wrappers.IExtraLabelsAware;
import org.rtsl.openmetrics.utils.wrappers.IMetricProviderNameAware;
import org.rtsl.openmetrics.utils.wrappers.IMetricProviderWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricProviderDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricProviderDecorator.class);
    private final List<Class<IMetricProviderWrapper>> wrapperClasses;

    public MetricProviderDecorator(List<Class<IMetricProviderWrapper>> wrapperClasses) {
        this.wrapperClasses = wrapperClasses;
    }

    public MetricProvider decorateMetricProvider(MetricProvider metricProvider, MetricMetadata metadata) throws Exception {

        MetricProvider currentMetricProvider = metricProvider;
        for (Class<IMetricProviderWrapper> currentWrapperClass : wrapperClasses) {
            IMetricProviderWrapper currentWrapper = currentWrapperClass.getConstructor().newInstance();
            LOGGER.info("Wrapping MetricProvider <{}> into Wrapper <{}>", currentMetricProvider, currentWrapper);
            currentWrapper.setMetricProvider(currentMetricProvider);
            if (currentWrapper instanceof IMetricProviderNameAware && metadata != null) {
                IMetricProviderNameAware currentAwareWrapper = (IMetricProviderNameAware) currentWrapper;
                currentAwareWrapper.setMetricProviderName(metadata.getFileName());
                //TODO : log
            }
            if (currentWrapper instanceof IExtraLabelsAware && metadata != null) {
                IExtraLabelsAware currentAwareWrapper = (IExtraLabelsAware) currentWrapper;
                currentAwareWrapper.setExtraLabels(metadata.getLabels());
                //TODO : log
            }
            currentMetricProvider = currentWrapper;

        }
        return currentMetricProvider;
    }

}
