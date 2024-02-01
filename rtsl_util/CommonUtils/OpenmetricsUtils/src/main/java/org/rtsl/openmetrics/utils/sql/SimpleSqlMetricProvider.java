package org.rtsl.openmetrics.utils.sql;

import java.util.List;

public final class SimpleSqlMetricProvider extends SqlMetricProvider {

    private List<SimpleRowConverter> metrics;
    
    public void setMetrics(List<SimpleRowConverter> metrics) {
        setConverters(metrics);
    }
    
}
