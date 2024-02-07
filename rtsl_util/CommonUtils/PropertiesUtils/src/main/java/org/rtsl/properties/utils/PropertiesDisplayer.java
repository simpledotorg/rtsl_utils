package org.rtsl.properties.utils;

import java.util.Properties;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PropertiesDisplayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesDisplayer.class);

    private TreeMap treeMapProperties;

    public void setProperties(Properties properties) {
        treeMapProperties = new TreeMap(properties);
    }

    public void display() throws Exception {
        LOGGER.info("Aggregated Properties are:");
        tabularDisplayInternal(treeMapProperties);
    }

    private static void tabularDisplayInternal(TreeMap treeMapProperties) {
        int maxsize = 0;
        for (Object currentKey : treeMapProperties.keySet()) {
            maxsize = Math.max(maxsize, currentKey.toString().length());
        }
        for (Object currentKey : treeMapProperties.keySet()) {
            Object currentValue = treeMapProperties.get(currentKey);
            String stringKey = currentKey.toString();
            LOGGER.info("{}\t{}", padRight(stringKey, maxsize), currentValue);
        }
    }

    public static void tabularDisplay(Properties properties) {
        TreeMap treeMapProperties = new TreeMap(properties);
        tabularDisplayInternal(treeMapProperties);
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

}
