package org.rtsl.openmetrics.utils.wrappers;

import org.rtsl.openmetrics.utils.MetricProvider;

public interface IMetricProviderWrapper extends MetricProvider {

    void setMetricProvider(MetricProvider wrappedMetricProvider);

}
