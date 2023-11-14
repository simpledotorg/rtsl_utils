package org.rtsl.openmetrics.utils;

import java.util.List;
import java.util.concurrent.Callable;

public interface MetricProvider<T extends Number> extends Callable<List<Metric<T>>>, IMetricSource {

    List<Metric<T>> getMetrics() throws Exception;

    @Override
    default String getAsString() throws Exception {
        StringBuilder sb = new StringBuilder();
        append(sb);
        return sb.toString();
    }

    @Override
    default void append(StringBuilder sb) throws Exception {
        for (Metric currentMetric : getMetrics()) {
            currentMetric.append(sb);
            sb.append("\n");
        }
    }

    @Override
    default List<Metric<T>> call() throws Exception { // An iterator could be more efficient ?
        return getMetrics();
    }
}
