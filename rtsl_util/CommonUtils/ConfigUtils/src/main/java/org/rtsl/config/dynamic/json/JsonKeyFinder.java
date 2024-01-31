package org.rtsl.config.dynamic.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonKeyFinder implements Function<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKeyFinder.class);
    public static final List<String> DEFAULT_JSON_KEYS = Collections.unmodifiableList(new ArrayList<>(Arrays.asList("type")));
    public static final String DEFAULT_SEPARATOR = "_";

    private final List<String> jsonKeys;
    private final String separator;

    public JsonKeyFinder(List<String> jsonKeys, String separator) {
        this.separator = separator;
        this.jsonKeys = jsonKeys;
    }

    public JsonKeyFinder() {
        this(DEFAULT_JSON_KEYS, DEFAULT_SEPARATOR);
    }

    public JsonKeyFinder(List<String> jsonKeys) {
        this(jsonKeys, DEFAULT_SEPARATOR);
    }

    @Override
    public String apply(String source) {
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        JsonObject jobj = new Gson().fromJson(source, JsonObject.class);
        boolean isFirst = true;
        for (String currentKey : jsonKeys) {
            LOGGER.trace("Getting value for key: <{}>", currentKey);
            String currentValue = jobj.get(currentKey).getAsString();
            LOGGER.debug("resulting key is <{}:{}>", currentKey, currentValue);
            sb.append(currentValue);
            if (!isFirst) {
                isFirst = false;
                sb.append(separator);
            }
        }
        String result = sb.toString();
        LOGGER.info("resulting key is <{}>", result);
        return result;
    }

}
