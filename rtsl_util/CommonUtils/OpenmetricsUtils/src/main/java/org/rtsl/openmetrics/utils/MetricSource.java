package org.rtsl.openmetrics.utils;

public interface MetricSource {

    String getAsString() throws Exception;

    void append(StringBuilder sb) throws Exception;

}
