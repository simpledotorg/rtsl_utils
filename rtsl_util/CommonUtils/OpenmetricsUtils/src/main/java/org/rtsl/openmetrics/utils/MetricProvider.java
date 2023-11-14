package org.rtsl.openmetrics.utils;

import java.util.List;
import java.util.concurrent.Callable;

public interface MetricProvider<T extends Number> extends Callable<List<Metric<T>>> {

    List<Metric<T>> getMetrics();

    default String getMetricsAsString() throws Exception{
        StringBuilder sb = new StringBuilder();
        appendMetrics(sb);
        return sb.toString();
    }

    default void appendMetrics(StringBuilder sb) throws Exception{
        for (Metric currentMetric : getMetrics()) {
            currentMetric.appendMetric(sb);
            sb.append("\n");
        }
    }

    default List<Metric<T>> call() throws Exception {
        return getMetrics();
    }
}
