package org.rtsl.properties.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesFilterValuesMappedToBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesFilterValuesMappedToBean.class);
    private Properties properties;
    private String prefix;

    public PropertiesFilterValuesMappedToBean() {
        super();
    }

    public PropertiesFilterValuesMappedToBean(Properties properties, String prefix) {
        super();
        this.properties = properties;
        this.prefix = prefix;
    }

    public void filterMappedValueToBean(Object obj) {

        if (null != prefix && !"".equals(prefix) && null != obj) {
            LOGGER.info(">>> Start filterMappedValueToBean prefix<{}>, input bean<{}>", prefix, obj.toString());
            for (Object k : properties.keySet()) {
                String key = (String) k;
                if (key.startsWith(prefix)) {
                    String name = key.substring(prefix.length() + 1);
                    String value = properties.getProperty(key);
                    try {
                        BeanUtils.setProperty(obj, name, value);
                    } catch (Exception e) {
                        LOGGER.error(name + " = " + value, e);
                    }
                }
            }
        } else {
            LOGGER.warn("The prefix or bean target is null or empty");
        }
    }

    public Map<String, String> getMapObjectBaddingPropertiesPrefix() {
        Map<String, String> result = new HashMap<String, String>();
        if (null != prefix && !"".equals(prefix)) {
            LOGGER.info(">>> Start getMapObjectBaddingPropertiesPrefix prefix<{}>", prefix);
            for (Object k : properties.keySet()) {
                String key = (String) k;
                if (key.startsWith(prefix)) {
                    String name = key.substring(prefix.length() + 1);
                    String value = properties.getProperty(key);
                    if (null != name && !"".equals(name)) {
                        result.put(name, value);
                    }
                }
            }
        }
        LOGGER.info("Map size<{}>", result.size());
        return result;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
