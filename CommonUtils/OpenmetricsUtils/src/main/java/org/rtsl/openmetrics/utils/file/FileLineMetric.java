package org.rtsl.openmetrics.utils.file;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.rtsl.openmetrics.utils.Metric;

public class FileLineMetric implements Metric<Number> {

    private static final String PARSING_REGEXP = "(.*)\\s+(\\d*.?\\d*)";
    private static final Pattern PATTERN = Pattern.compile(PARSING_REGEXP);

    private final String rawLine;
    private final String fullName;
    private final String value;

    public FileLineMetric(String rawLine) {
        this.rawLine = rawLine;
        Matcher matcher = PATTERN.matcher(rawLine);
        if (matcher.find()) {
            fullName = matcher.group(0);
            value = matcher.group(1);
        } else {
            throw new IllegalArgumentException("Input string is not a valid metric: " + rawLine);
        }

    }

    @Override
    public String getMetricFullName() {
        return fullName;
    }

    @Override
    public Number getMetricValue() {
        return new BigDecimal(value);
    }

    @Override
    public void append(StringBuilder sb) {
        sb.append(rawLine);
    }

    @Override
    public String getAsString() {
        return rawLine;
    }

}
