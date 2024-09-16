package org.rtsl.dhis2.cucumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;

public class Period {

    private static final LocalDateTime now = LocalDateTime.now();
    private static final LocalDate today = LocalDate.now();
    final static DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final static DateTimeFormatter REPORTING_YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    final static DateTimeFormatter REPORTING_YEAR_QUARTER_FORMATTER = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR).appendLiteral('Q').appendValue(IsoFields.QUARTER_OF_YEAR).toFormatter();

    public static String toDateString(String relativeDate) {
        return parseRelativeDate(relativeDate).format(YEAR_MONTH_FORMATTER);
    }

    public static String toReportingDateString(String relativeDate, String periodType) {
        if (periodType.equalsIgnoreCase("Months"))
            return parseRelativeDate(relativeDate).format(REPORTING_YEAR_MONTH_FORMATTER);
        else
            return parseRelativeDate(relativeDate).format(REPORTING_YEAR_QUARTER_FORMATTER);
    }

    public static LocalDate toDate(String relativeDate) {
        return parseRelativeDate(relativeDate);
    }

    public static LocalDate parseRelativeDate(String relativeDate) {
        // Split the relative date string by "_"
        String[] parts = relativeDate.split("_");

        // Initialize the finalDate as the current date
        LocalDate finalDate = today;

        // Loop through the parts and apply the changes to the date
        for (int i = 0; i < parts.length; i++) {
            switch (parts[i]) {
                case "thisQuarter":
                    // Calculate the start of the current quarter
                    finalDate = today.with(IsoFields.DAY_OF_QUARTER, 1L);
                    break;
                case "thisMonth":
                    // Calculate the start of the current month
                    finalDate = today.withDayOfMonth(1);
                    break;
                case "BeginningOfMonth":
                    // Set to the beginning of the current month
                    finalDate = finalDate.withDayOfMonth(1);
                    break;
                case "EndOfMonth":
                    // Set to the end of the month
                    finalDate = finalDate.with(TemporalAdjusters.lastDayOfMonth());
                    break;
                case "MonthsAgo", "MonthAgo":
                    // Get the number before "MonthsAgo" and adjust the date
                    int monthsAgo = Integer.parseInt(parts[i - 1]);
                    finalDate = finalDate.minusMonths(monthsAgo).withDayOfMonth(1);
                    break;
                case "QuartersAgo", "QuarterAgo":
                    // Get the number before "QuartersAgo" and adjust the date
                    int quartersAgo = Integer.parseInt(parts[i - 1]);
                    finalDate = finalDate.with(IsoFields.DAY_OF_QUARTER, 1L).minusMonths(quartersAgo * 3L);
                    break;
                case "DaysAgo":
                    // Get the number before "DaysAgo" and adjust the date
                    int daysAgo = Integer.parseInt(parts[i - 1]);
                    finalDate = finalDate.minusDays(daysAgo);
                    break;
                case "Plus":
                    // Check for whether to add months or days
                    if (parts[i + 2].equals("Months") || parts[i + 2].equals("Month")) {
                        int monthsToAdd = Integer.parseInt(parts[i + 1]);
                        finalDate = finalDate.plusMonths(monthsToAdd);
                    } else if (parts[i + 2].equals("Days") || parts[i + 2].equals("Day")) {
                        int daysToAdd = Integer.parseInt(parts[i + 1]);
                        finalDate = finalDate.plusDays(daysToAdd);
                    }
                    i += 2;
                    break;
                case "Minus":
                    // Handle subtraction for months or days
                    if (parts[i + 2].equals("Months") || parts[i + 2].equals("Month")) {
                        int monthsToSubtract = Integer.parseInt(parts[i + 1]);
                        finalDate = finalDate.minusMonths(monthsToSubtract);
                    } else if (parts[i + 2].equals("Days") || parts[i + 2].equals("Day")) {
                        int daysToSubtract = Integer.parseInt(parts[i + 1]);
                        finalDate = finalDate.minusDays(daysToSubtract);
                    }
                    i += 2;
                    break;
            }
        }

        return finalDate;
    }
}
