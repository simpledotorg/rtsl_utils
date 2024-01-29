package org.rtsl.openmetrics.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;

public class FileMetricProvider implements MetricProvider {

    private File metricFile;

    public void setMetricFile(File metricFile) {
        this.metricFile = metricFile;
    }

    public void setMetricFile(String metricFileName) {
        this.metricFile = new File(metricFileName);
    }

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

}
