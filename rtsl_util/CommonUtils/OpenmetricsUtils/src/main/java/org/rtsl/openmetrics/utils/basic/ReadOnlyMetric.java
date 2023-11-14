package org.rtsl.openmetrics.utils.basic;

import org.rtsl.openmetrics.utils.Metric;

public final class ReadOnlyMetric<T extends Number> implements Metric<T> {

    private final Metric<T> wrappedMetric;

    public ReadOnlyMetric(Metric<T> wrappedMetric) {
        this.wrappedMetric = wrappedMetric;
    }

    @Override
    public String getMetricFullName() {
        return wrappedMetric.getMetricFullName();
    }

    @Override
    public T getMetricValue() {
        return wrappedMetric.getMetricValue();
    }

}
