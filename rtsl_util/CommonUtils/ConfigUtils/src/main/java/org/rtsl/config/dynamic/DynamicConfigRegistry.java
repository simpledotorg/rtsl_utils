package org.rtsl.config.dynamic;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DynamicConfigRegistry<SOURCE, KEY, TARGET> implements Function<SOURCE, TARGET> {

    private final Function<SOURCE, KEY> getKeyFunction;
    private final Function<KEY, String> getIdentifier;
    private final Map<String, List<Function>> factories;

    public DynamicConfigRegistry(Function<SOURCE, KEY> getKeyFunction, Map<String, List<Function>> factories, Function<KEY, String> getIdentifier) {
        this.getKeyFunction = getKeyFunction;
        this.getIdentifier = getIdentifier;
        this.factories = factories;
    }

    public DynamicConfigRegistry(Function<SOURCE, KEY> getKeyFunction, Map<String, List<Function>> factories) {
        this(getKeyFunction, factories, new DefaultIdentifierGetter<KEY>());
    }

    @Override
    public TARGET apply(SOURCE source) {
        KEY key = getKeyFunction.apply(source);
        String identifier = getIdentifier.apply(key);
        List<Function> currentFunctions = factories.get(identifier);
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
        TARGET finalObject = null;
        try {
            finalObject = (TARGET) currentObject;
        } catch (Exception ex) {
            // TODO Log WARN
        }
        return finalObject;
    }

    public Function<SOURCE, KEY> getGetKeyFunction() {
        return getKeyFunction;
    }

}
