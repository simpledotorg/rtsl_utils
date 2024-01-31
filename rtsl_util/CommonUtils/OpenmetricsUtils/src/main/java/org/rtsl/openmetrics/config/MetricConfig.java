package org.rtsl.openmetrics.config;

public class MetricConfig {

    private String name;
    private String type;
    private Boolean asynch;
    private String cron;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAsynch() {
        return asynch;
    }

    public void setAsynch(Boolean asynch) {
        this.asynch = asynch;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
