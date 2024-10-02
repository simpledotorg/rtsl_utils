package org.rtsl.properties.utils.modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObfuscationPropertyModifier implements Function<Properties, Properties> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObfuscationPropertyModifier.class);
    public static final String DEFAULT_OBFUSCATION_PREFIX = "__hidden__.";
    public static final String DEFAULT_OBFUSCATION_TEXT = "<hidden>";

    private List<String> obfuscationFlagPrefixes = Collections.unmodifiableList(Arrays.asList(DEFAULT_OBFUSCATION_PREFIX, EncryptionPropertyModifier.DEFAULT_ENCRYPTED_PREFIX));
    private String obfuscationText = DEFAULT_OBFUSCATION_TEXT;
    private List< String> encryptionFlagExpectedValues = new ArrayList<>(Arrays.asList(
            "1", "true"));

    public void setObfuscationFlagPrefixes(List<String> obfuscationFlagPrefixes) {
        this.obfuscationFlagPrefixes = obfuscationFlagPrefixes;
    }

    public void setObfuscationText(String obfuscationText) {
        this.obfuscationText = obfuscationText;
    }

    public void setEncryptionFlagExpectedValues(List<String> encryptionFlagExpectedValues) {
        this.encryptionFlagExpectedValues = encryptionFlagExpectedValues;
    }

    @Override
    public Properties apply(Properties inputProperties) {
        Properties returnProperties = new Properties();
        returnProperties.putAll(inputProperties);
        for (Object currentKey : returnProperties.keySet()) {
            for (String currentPrefixAttempt : obfuscationFlagPrefixes) {
                Object value = returnProperties.get(currentKey);
                String isEncrytedString = inputProperties.getProperty(currentPrefixAttempt + currentKey);
                if (encryptionFlagExpectedValues.contains(isEncrytedString) && value != null && value instanceof String) {
                    LOGGER.info("Overriding value of key <{}> with obfuscated value", currentKey);
                    String stringValue = (String) value;
                    returnProperties.put(currentKey, obfuscationText);
                }
            }
        }
        return returnProperties;
    }

}
