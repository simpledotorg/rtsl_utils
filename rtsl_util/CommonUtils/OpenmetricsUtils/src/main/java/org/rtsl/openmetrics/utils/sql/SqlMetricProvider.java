package org.rtsl.openmetrics.utils.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;

public class SqlMetricProvider implements MetricProvider, DataSourceAware {

    private DataSource dataSource = null;
    private String query;
    private List<? extends RowConverter> converters;

    @Override
    public final void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public final void setQuery(String query) {
        this.query = query;
    }

    public final void setConverters(List<? extends RowConverter> converters) {
        this.converters = converters;
    }

    @Override
    public final List<Metric> getMetrics() {
        List<Metric> returnMetrics = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {// for each line
                    Map<String, Object> data = resultSetToMap(resultSet);
                    for (RowConverter currentRowConverter : converters) {// for each converter
                        // TODO logs and stuff
                        returnMetrics.add(currentRowConverter.getMetric(data));
                    }
                }
            }
        } catch (Exception ex) {
            // TODO 
        }
        return returnMetrics;
    }

    private final Map<String, Object> resultSetToMap(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object columnValue = rs.getObject(i);
            row.put(columnName, columnValue);
        }
        return row;
    }

}
