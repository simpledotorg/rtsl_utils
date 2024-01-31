package org.rtsl.openmetrics.config;

import java.util.function.Function;

public class MetricConfigTypeGetter implements Function<MetricConfig, String> {

    @Override
    public String apply(MetricConfig t) {
        return t.getType();
    }

}
