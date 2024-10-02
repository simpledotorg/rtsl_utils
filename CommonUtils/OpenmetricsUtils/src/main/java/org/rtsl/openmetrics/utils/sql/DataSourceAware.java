package org.rtsl.openmetrics.utils.sql;

import javax.sql.DataSource;

public interface DataSourceAware {

    void setDataSource(DataSource dataSource);
}
