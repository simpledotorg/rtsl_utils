package org.rtsl.openmetrics.config;

import java.util.function.Function;

public final class MetricMetadataTypeGetter implements Function<MetricMetadata, String> {

    @Override
    public String apply(MetricMetadata t) {
        return t.getType();
    }

}
