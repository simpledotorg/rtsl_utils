package org.rtsl.openmetrics.utils.sql;

import java.util.function.Function;
import javax.sql.DataSource;

public class DataSourceInjector implements Function<DataSourceAware, DataSourceAware> {

    private final DataSource dataSource;

    public DataSourceInjector(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSourceAware apply(DataSourceAware t) {
        t.setDataSource(dataSource);
        return t;
    }

}
