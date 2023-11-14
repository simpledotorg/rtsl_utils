package org.rtsl.openmetrics.utils;

import java.io.Serializable;

public interface Metric<T extends Number> extends Serializable, Comparable<Metric>, Cloneable {

    String getMetricFullName();

    T getMetricValue();

    default String getMetric() {
        return getMetricFullName() + (" " + getMetricValue());
    }

    default void appendMetric(StringBuilder sb) {
        sb.append(getMetricFullName()).append(" ").append(getMetricValue());
    }

    @Override
    default int compareTo(Metric candidate) {
        return getMetricFullName().compareTo(candidate.getMetricFullName());
    }

}
