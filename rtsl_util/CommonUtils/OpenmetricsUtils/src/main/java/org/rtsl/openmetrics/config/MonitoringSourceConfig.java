package org.rtsl.openmetrics.config;

public interface MonitoringSourceConfig {

    void setAsynch(boolean asynch);

    void setType(String type);

    boolean isAsynch();

    String getType();

}
