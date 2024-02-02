package org.rtsl.openmetrics.utils.asynch;

import java.util.List;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;

public interface IMetricCache extends MetricProvider {

    void cacheMetrics(List<Metric> metrics);

}
