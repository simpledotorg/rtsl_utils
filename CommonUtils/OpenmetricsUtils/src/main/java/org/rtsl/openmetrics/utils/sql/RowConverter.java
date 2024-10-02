package org.rtsl.openmetrics.utils.sql;

import java.util.Map;
import org.rtsl.openmetrics.utils.Metric;

public interface RowConverter {

    Metric getMetric(Map<String, Object> row);

}
