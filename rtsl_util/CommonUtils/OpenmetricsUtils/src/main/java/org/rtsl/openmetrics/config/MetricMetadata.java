package org.rtsl.openmetrics.config;

import java.util.HashMap;
import java.util.Map;

public class MetricMetadata implements Cloneable {

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

    public Map<String, String> getLabels() {
        Map<String, String> returnMap = new HashMap<>();
        if (name != null) {
            returnMap.put("monitoring_source_name", name);
        }
        if (asynch != null) {
            returnMap.put("asynch", asynch.toString());
        }
        if (type != null) {
            returnMap.put("type", type);
        }
        return returnMap;

    }

    @Override
    protected MetricMetadata clone() throws CloneNotSupportedException {
        return (MetricMetadata) super.clone();
    }

}
