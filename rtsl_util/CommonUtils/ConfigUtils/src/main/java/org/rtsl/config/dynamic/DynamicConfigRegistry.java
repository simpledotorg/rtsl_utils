package org.rtsl.config.dynamic;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DynamicConfigRegistry<S, K> implements Function<S, K> {

    private final Function<S, String> getKeyFunction;
    private final Map<String, List<Function>> factories;

    public DynamicConfigRegistry(Function<S, String> getKeyFunction, Map<String, List<Function>> factories) {
        this.getKeyFunction = getKeyFunction;
        this.factories = factories;
    }

    @Override
    public K apply(S source) {
        String key = getKeyFunction.apply(source);
        List<Function> currentFunctions = factories.get(key);
        if (currentFunctions == null) {
            // TODO warn
            return null;
        }
        if (currentFunctions.size() < 1) {
            // TODO warn
            return null;
        }
        Object currentObject = source;
        for (Function currentFactory : currentFunctions) {
            currentObject = currentFactory.apply(currentObject);
        }
        K finalObject = null;
        try {
            finalObject = (K) currentObject;
        } catch (Exception ex) {
            // TODO Log WARN
        }
        return finalObject;
    }

    public Function<S, String> getGetKeyFunction() {
        return getKeyFunction;
    }

}
