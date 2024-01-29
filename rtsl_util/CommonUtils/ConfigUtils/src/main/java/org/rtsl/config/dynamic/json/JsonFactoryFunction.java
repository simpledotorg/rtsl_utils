package org.rtsl.config.dynamic.json;

import com.google.gson.Gson;
import java.util.function.Function;

public class JsonFactoryFunction<K> implements Function<String, K> {

    private final Class<K> clazz;

    public JsonFactoryFunction(Class<K> clazz) {
        this.clazz = clazz;
    }

    @Override
    public K apply(String inputJsonString) {
        Gson gson = new Gson();
        return gson.fromJson(inputJsonString, clazz);
    }

}
