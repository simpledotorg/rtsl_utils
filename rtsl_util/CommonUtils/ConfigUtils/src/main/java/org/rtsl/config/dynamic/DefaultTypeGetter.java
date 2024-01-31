package org.rtsl.config.dynamic;

import java.util.function.Function;

public class DefaultTypeGetter<KEY> implements Function<KEY, String> {

    @Override
    public String apply(KEY t) {
        return t.toString();
    }

}
