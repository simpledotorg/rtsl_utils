package org.rtsl.openmetrics.config;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.rtsl.openmetrics.utils.MetricProvider;
import org.rtsl.openmetrics.utils.asynch.MetricCachingConsumer;
import org.rtsl.openmetrics.utils.basic.ParallelMetricProvider;
import org.rtsl.openmetrics.utils.file.FileMetricProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecoratingParallelMetricProvider implements MetricProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecoratingParallelMetricProvider.class);

    private final ParallelMetricProvider wrappedParallelMetricProvider;
    private final File cacheFolder;

    public DecoratingParallelMetricProvider(Map<MetricMetadata, MetricProvider> providers, File cacheFolder, MetricProviderDecorator decorator, int processingPoolSize) throws Exception {
        this.cacheFolder = cacheFolder;
        LOGGER.info("Using folder <{}> for cache (and creating it if it does not exist).", cacheFolder.toPath());
        Files.createDirectories(cacheFolder.toPath());
        List<MetricProvider> wrappedMetrics = getWrappedMetrics(providers, decorator);
        wrappedParallelMetricProvider = new ParallelMetricProvider(wrappedMetrics, processingPoolSize);

    }

    private List<MetricProvider> getWrappedMetrics(Map<MetricMetadata, MetricProvider> providers, MetricProviderDecorator decorator) throws Exception {
        List<MetricProvider> returnList = new ArrayList<>();
        for (MetricMetadata currentMetadata : providers.keySet()) {
            MetricProvider currentProvider = providers.get(currentMetadata);
            MetricProvider decoratedProvider = decorator.decorateMetricProvider(currentProvider, currentMetadata);
            if (currentMetadata.getAsynch()) {// MetricProvider is configured as asynch. Lets use some cache !
                MetricMetadata cacheMetaData = currentMetadata.clone();
                cacheMetaData.setAsynch(false);
                if (cacheMetaData.getType() != null) {
                    cacheMetaData.setType(cacheMetaData.getType() + "_cache");
                }
                File cacheFile = new File(cacheFolder, cacheMetaData.getName() + ".cache.prom");
                FileMetricProvider provider = new FileMetricProvider();
                provider.setMetricFile(cacheFile);
                MetricCachingConsumer cache = new MetricCachingConsumer(decoratedProvider, provider);
                MetricProvider decoratedCache = decorator.decorateMetricProvider(cache, cacheMetaData);
                returnList.add(decoratedCache);
                cache.run();
                // TODO : sumbit decroratedcache
            } else {
                returnList.add(decoratedProvider);
            }
        }
        return returnList;
    }

    @Override
    public List getMetrics() throws Exception {
        return wrappedParallelMetricProvider.getMetrics();
    }

}
