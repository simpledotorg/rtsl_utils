package org.rtsl.tests.config.beans;

import java.util.List;
import java.util.Map;

public class TestConfigBean1 {

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
        private List<InternalMetricsClass> metrics;

        public List<InternalMetricsClass> getMetrics() {
            return metrics;
        }

        public void setMetrics(List<InternalMetricsClass> metrics) {
            this.metrics = metrics;
        }

        public String getSqlQuery() {
            return sqlQuery;
        }

        public void setSqlQuery(String sqlQuery) {
            this.sqlQuery = sqlQuery;
        }

        public class InternalMetricsClass {

            private String name;
            private String valueSourceColumn;
            private Map<String, String> labels;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValueSourceColumn() {
                return valueSourceColumn;
            }

            public void setValueSourceColumn(String valueSourceColumn) {
                this.valueSourceColumn = valueSourceColumn;
            }

            public Map<String, String> getLabels() {
                return labels;
            }

            public void setLabels(Map<String, String> labels) {
                this.labels = labels;
            }

        }

    }

}
