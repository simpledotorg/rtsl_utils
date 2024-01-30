package org.rtsl.openmetrics.config;

import java.util.List;
import java.util.function.Function;
import javax.sql.DataSource;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.sql.RowConverter;
import org.rtsl.openmetrics.utils.sql.SqlMetricProvider;

public class SqlMetricFactory implements Function<SqlMetricProviderConfig, MetricProvider> {
    
    private DataSource dataSource = null;
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public MetricProvider apply(SqlMetricProviderConfig config) {
        SqlMetricProvider metricProvider = new SqlMetricProvider();
        metricProvider.setDataSource(dataSource);
        metricProvider.setQuery(config.getConfig().getSqlQuery());
        List<? extends RowConverter> converters =  config.getConfig().getMetrics();
        metricProvider.setConverters(converters);
        return metricProvider;
    }
    
}
