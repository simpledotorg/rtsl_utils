package org.rtsl.properties.utils.modifier;

import org.rtsl.properties.utils.PropertiesDisplayer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InjectingPropertiesModifier implements Function<Properties, Properties> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InjectingPropertiesModifier.class);
    private static final String PLACEHOLDER_REGEXP = "\\$\\{([^\\$\\{\\}]*)\\}";
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(PLACEHOLDER_REGEXP);
    private int maxLoops = 10; // to make sure it ends at some point
    private boolean injectSystemProperties = false;

    public void setMaxLoops(int maxLoops) {
        this.maxLoops = maxLoops;
    }

    public void setInjectSystemProperties(boolean injectSystemProperties) {
        this.injectSystemProperties = injectSystemProperties;
    }

    @Override
    public Properties apply(Properties inputProperties) {
        int nbModification = Integer.MAX_VALUE;
        int nbLoops = 0;
        Map<String, String> cyclicKeys = new HashMap<>();
        Properties referenceProperties = getReferenceProperties(inputProperties);
        Properties returnProperties = new Properties();
        returnProperties.putAll(inputProperties);
        while (nbModification > 0 && nbLoops < maxLoops) {
            nbLoops++;
            nbModification = 0;
            LOGGER.debug("Injection loop <{}> staring", nbLoops);
            for (Object currentKey : returnProperties.keySet()) {
                Object value = returnProperties.get(currentKey);
                if (value instanceof String && !cyclicKeys.containsKey(currentKey)) {
                    String stringValue = (String) value;
                    Matcher matcher = PLACEHOLDER_PATTERN.matcher(stringValue);
                    int localModified = 0;
                    int groupPosition = 0;
                    while (matcher.find()) {
                        groupPosition++;
                        String placeholder = matcher.group(1);
                        LOGGER.debug("Found key to be replaced <{}> for key <{}> (match <{}>) ", placeholder, currentKey, groupPosition);
                        if (!placeholder.equals(currentKey.toString())) {
                            if (!cyclicKeys.containsKey(placeholder)) {
                                String newValue = referenceProperties.getProperty(placeholder);
                                if (newValue != null) {
                                    stringValue = getNewValue(stringValue, placeholder, newValue);
                                    localModified++;
                                } else {
                                    LOGGER.debug("Keeping placeholder <{}> as it is not present in the properties", placeholder);
                                }
                            } else {
                                LOGGER.warn("Key <{}> flagged as having circular dependency. Skipping", placeholder);;
                                cyclicKeys.put(placeholder, placeholder);
                            }
                        } else {
                            LOGGER.warn("Circular dependency detected on key <{}>. Skipping", placeholder);
                            cyclicKeys.put(placeholder, placeholder);
                        }
                    }
                    if (localModified > 0) {
                        LOGGER.debug("Replacing value for key <{}> with <{}> modifications", currentKey, localModified);
                        returnProperties.put(currentKey, stringValue);
                        nbModification++;
                    }
                }
            }
            if (maxLoops <= nbLoops) {
                LOGGER.warn("Reach maximum number of properties substition loops ({}) without resolving everything.", maxLoops);
            }
            LOGGER.info("Loop <{}> finished with <{}> replacements", nbLoops, nbModification);
        }
        return returnProperties;
    }

    private static String getNewValue(String stringValue, String placeholder, String newValue) {
        return stringValue.replaceFirst(Pattern.quote("${" + placeholder + "}"), Matcher.quoteReplacement(newValue));
    }

    private Properties getReferenceProperties(Properties inputProperties) {
        Properties returnProperties = new Properties();
        if (injectSystemProperties) { // in case we want to also use system properties.
            LOGGER.info("Environement variables will be used for substitution");
            returnProperties.putAll(System.getenv());
            if (LOGGER.isTraceEnabled()) {
                LOGGER.debug("Details of Environment Variables usable for substitution:");
                PropertiesDisplayer.tabularDisplay(returnProperties);
            }
        }
        returnProperties.putAll(inputProperties);
        return returnProperties;
    }

}
