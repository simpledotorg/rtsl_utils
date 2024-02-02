package org.rtsl.openmetrics.utils.wrappers;

import java.util.Map;

public interface IExtraLabelsAware {

    void setExtraLabels(Map<String, String> extraLabels);
}
