package org.rtsl.openmetrics.utils.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;

public class FileWriterMetricConsumer implements Runnable {

    private File metricFile;
    private MetricProvider<Number> metricProvider;

    public void setMetricFile(File metricFile) {
        this.metricFile = metricFile;
    }

    public void setMetricFile(String metricFileName) {
        this.metricFile = new File(metricFileName);
    }

    public void setMetricProvider(MetricProvider metricProvider) {
        this.metricProvider = metricProvider;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(metricFile))) {
            for (Metric currentMetric : metricProvider.getMetrics()) {
                writer.write(currentMetric.getAsString());
                writer.write("\n");
            }
        } catch (Exception e) {
            // TODO : log
        }
    }

}
