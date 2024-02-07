package org.rtsl.properties.utils.modifier;

import java.util.Properties;
import java.util.function.Function;

public class CopyingPropertyModifier implements Function<Properties, Properties> {

    @Override
    public Properties apply(Properties inputProperties) {
        Properties returnProperties = new Properties();
        returnProperties.putAll(inputProperties);
        return returnProperties;
    }

}
