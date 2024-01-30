package org.rtsl.openmetrics.config;

import java.util.List;
import org.rtsl.openmetrics.utils.sql.SimpleRowConverter;

public class SqlMetricProviderConfig {

    private String type;
    private Boolean asynch;
    private InternalConfigClass config;

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

    public InternalConfigClass getConfig() {
        return config;
    }

    public void setConfig(InternalConfigClass config) {
        this.config = config;
    }

    public class InternalConfigClass {

        private String sqlQuery;
        private List<SimpleRowConverter> metrics;

        public String getSqlQuery() {
            return sqlQuery;
        }

        public void setSqlQuery(String sqlQuery) {
            this.sqlQuery = sqlQuery;
        }

        public List<SimpleRowConverter> getMetrics() {
            return metrics;
        }

        public void setMetrics(List<SimpleRowConverter> metrics) {
            this.metrics = metrics;
        }

    }

}
