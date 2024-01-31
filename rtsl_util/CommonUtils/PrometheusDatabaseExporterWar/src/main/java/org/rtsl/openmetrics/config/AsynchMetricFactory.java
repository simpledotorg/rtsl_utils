package org.rtsl.openmetrics.config;

import java.util.function.Function;
import org.rtsl.openmetrics.utils.MetricProvider;

public class AsynchMetricFactory<K extends MetricProvider> implements Function<SqlMetricProviderConfig, K> {

    @Override
    public K apply(SqlMetricProviderConfig t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
