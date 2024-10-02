package org.rtsl.openmetrics.utils.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.asynch.IMetricCache;

public class FileMetricProvider implements MetricProvider, IMetricCache {

    private File metricFile;

    public void setMetricFile(File metricFile) {
        this.metricFile = metricFile;
    }

    public void setMetricFile(String metricFileName) {
        this.metricFile = new File(metricFileName);
    }

    // TODO :prevent read and write at the same time
    @Override
    public List getMetrics() throws IOException {
        ArrayList<Metric> returnMetrics = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(metricFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    Metric currentMetric = new FileLineMetric(line.trim());
                    returnMetrics.add(currentMetric);
                }
            }
        }
        return returnMetrics;
    }

    // TODO :prevent read and write at the same time. Just in case.
    @Override
    public void cacheMetrics(List<Metric> metrics) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(metricFile))) {
            for (Metric currentMetric : metrics) {
                writer.write(currentMetric.getAsString());
                writer.write("\n");
            }
        } catch (Exception e) {
            // TODO : log
        }
    }

}
