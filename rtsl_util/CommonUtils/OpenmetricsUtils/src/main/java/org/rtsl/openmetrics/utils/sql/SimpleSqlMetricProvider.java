package org.rtsl.openmetrics.utils.sql;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimpleSqlMetricProvider extends SqlMetricProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSqlMetricProvider.class);
    private List<SimpleRowConverter> metrics;

    public void setMetrics(List<SimpleRowConverter> metrics) {
        LOGGER.debug("Adding <{}> SimpleRowConverters to <{}>", metrics.size(), this);
        this.setConverters(metrics);
        this.metrics = metrics;
    }

}
