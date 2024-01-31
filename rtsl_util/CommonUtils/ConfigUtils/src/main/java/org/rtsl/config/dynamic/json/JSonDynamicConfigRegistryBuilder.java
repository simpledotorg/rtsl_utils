package org.rtsl.config.dynamic.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.rtsl.config.dynamic.DefaultIdentifierGetter;
import org.rtsl.config.dynamic.DynamicConfigRegistry;

public class JSonDynamicConfigRegistryBuilder<KEY, TARGET> {

    private Function<String, KEY> getKeyFunction;
    private Function<KEY, String> getIdentifier = new DefaultIdentifierGetter<>();
    private Map<String, Class> configClasses = new HashMap<>();
    private Map<String, Function> additionalFactories = new HashMap<>();

    public DynamicConfigRegistry<String, KEY, TARGET> build() {
        return new DynamicConfigRegistry<>(getKeyFunction, getAllFactories(), getIdentifier);
    }

    private Map<String, List<Function>> getAllFactories() {
        Map<String, List<Function>> returnMap = new HashMap<>();
        for (String currentKey : configClasses.keySet()) {
            Class currentClass = configClasses.get(currentKey);
            Function currentConfigFactory = new JsonFactoryFunction(currentClass);
            List<Function> currentList = new ArrayList<>(Arrays.asList(currentConfigFactory));
            if (additionalFactories.get(currentKey) != null) {
                //TODO : log INFO
                currentList.add(additionalFactories.get(currentKey));
            }
            returnMap.put(currentKey, currentList);
        }
        return returnMap;
    }

    public void setGetKeyFunction(Function<String, KEY> getKeyFunction) {
        this.getKeyFunction = getKeyFunction;
    }

    public void setGetIdentifier(Function<KEY, String> getIdentifier) {
        this.getIdentifier = getIdentifier;
    }

    public void setConfigClasses(Map<String, Class> configClasses) {
        this.configClasses = configClasses;
    }

    public void setAdditionalFactories(Map<String, Function> additionalFactories) {
        this.additionalFactories = additionalFactories;
    }

}
