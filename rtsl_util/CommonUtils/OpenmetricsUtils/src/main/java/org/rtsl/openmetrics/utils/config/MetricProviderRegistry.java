
package org.rtsl.openmetrics.utils.config;

import org.rtsl.openmetrics.utils.MetricProvider;


public interface MetricProviderRegistry<K> {
    
    
    MetricProvider getMetricProvider(K Key);
    
}
