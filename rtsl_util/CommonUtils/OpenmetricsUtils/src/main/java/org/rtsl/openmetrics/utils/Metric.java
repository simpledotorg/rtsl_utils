package org.rtsl.openmetrics.utils;

import java.io.Serializable;

public interface Metric<T extends Number> extends Serializable, Comparable<Metric>, Cloneable, IMetricSource {

    String getMetricFullName();

    T getMetricValue();

    @Override
    default String getAsString() {
        return getMetricFullName() + (" " + getMetricValue());
    }

    @Override
    default void append(StringBuilder sb) {
        sb.append(getMetricFullName()).append(" ").append(getMetricValue());
    }

    @Override
    default int compareTo(Metric candidate) {
        return getMetricFullName().compareTo(candidate.getMetricFullName());
    }

}
