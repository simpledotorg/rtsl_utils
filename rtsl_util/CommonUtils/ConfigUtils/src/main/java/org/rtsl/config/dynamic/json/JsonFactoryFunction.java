package org.rtsl.config.dynamic.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonFactoryFunction<K> implements Function<String, K> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFactoryFunction.class);
    private final ObjectMapper objectMapper;

    private final Class<K> clazz;

    public JsonFactoryFunction(Class<K> clazz) {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        this.clazz = clazz;
    }

    @Override
    public K apply(String inputJsonString) {
        try {
            K returnObject = objectMapper.readValue(inputJsonString, clazz);
            if (LOGGER.isDebugEnabled()) {
                try {
                    LOGGER.debug("Resulting object is : {}", objectMapper.writeValueAsString(returnObject));

                } catch (Exception ex) {
                    LOGGER.warn("Cannot log resulting object. Exception is : ", ex);
                }
            }
            return returnObject;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
