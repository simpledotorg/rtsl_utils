package org.rtsl.openmetrics.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelMetricProvider implements MetricProvider {

    private final List<MetricProvider> providers;
    private final ExecutorService executor;

    public ParallelMetricProvider(List<MetricProvider> providers, int processingPoolSize) {
        this.providers = providers;
        executor = Executors.newFixedThreadPool(processingPoolSize);
    }

    public ParallelMetricProvider(List<MetricProvider> providers) {
        this(providers, 10);
    }

    @Override
    public List getMetrics() {
        ArrayList<Metric> returnMetrics = new ArrayList<>();
        for (MetricProvider currentProvider : providers) {
            returnMetrics.addAll(currentProvider.getMetrics());
        }
        List<Future<List<Metric>>> futures = new ArrayList<>();
        for (MetricProvider currentMetricProvider : providers) {
            futures.add(executor.submit(currentMetricProvider));
        }

        for (Future<List<Metric>> currentFuture : futures) {
            try {
                List<Metric> currentResult = currentFuture.get();
                returnMetrics.addAll(currentResult);
            } catch (Exception ex) {
                // TODO : log & error management
            }
        }
        return returnMetrics;
    }

}
