package org.rtsl.openmetrics.config;

import java.util.HashMap;
import java.util.Map;
import org.rtsl.config.dynamic.folder.FileNameAware;

public class MetricMetadata implements Cloneable, FileNameAware {

    private String name;
    private String type;
    private Boolean asynch;
    private String cron;
    private String fileName;

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

    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getLabels() {
        Map<String, String> returnMap = new HashMap<>();
        if (name != null) {
            returnMap.put("name", name);
        }
        if (asynch != null) {
            returnMap.put("asynch", asynch.toString());
            if (asynch && cron != null) {
                returnMap.put("cron", cron);
            }
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
