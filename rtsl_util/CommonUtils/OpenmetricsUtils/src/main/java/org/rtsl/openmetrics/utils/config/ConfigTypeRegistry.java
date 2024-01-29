
package org.rtsl.openmetrics.utils.config;

import java.util.Map;


public class ConfigTypeRegistry {
    
    private final Map<String, Class<MonitoringSourceConfig>> configClasses;

    public ConfigTypeRegistry(Map<String, Class<MonitoringSourceConfig>> configClasses) {
        this.configClasses = configClasses;
    }
    

    
    
}
