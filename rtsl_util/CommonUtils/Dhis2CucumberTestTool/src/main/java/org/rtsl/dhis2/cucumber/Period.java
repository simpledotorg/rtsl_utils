package org.rtsl.dhis2.cucumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.util.ArrayList;

public class Period {

    private static LocalDateTime now = LocalDateTime.now();
    private static LocalDate today = LocalDate.now();
    final static DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    final static DateTimeFormatter YEAR_QUARTER_FORMATTER = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR).appendLiteral('Q').appendValue(IsoFields.QUARTER_OF_YEAR).toFormatter();

    public static LocalDate getToday() {
        return today;
    }

    public static LocalDateTime getNow() {
        return now;
    }

    public void setToday(LocalDate today) {
        Period.today = today;
        Period.now = Period.today.atStartOfDay();
    }

    public static void setToday(CharSequence today) {
        Period.today = LocalDate.parse(today);
        Period.now = Period.today.atStartOfDay();
    }

    public void setNow(LocalDateTime now) {
        Period.now = now;
        today = Period.now.toLocalDate();
    }

    public void setNow(CharSequence now) {
        Period.now = LocalDateTime.parse(now);
        today = Period.now.toLocalDate();
    }

    public static String thisMonth() {
        return toMonthString(today);
    }

    public static String lastMonth() {
        return toMonthString(today.minusMonths(1));
    }
    public static ArrayList<String> lastTwelveMonths() {
        return lastNMonths(12);
    }

    public static ArrayList<String> lastSixMonths() {
        return lastNMonths(6);
    }

    public static ArrayList<String> lastNMonths(int n) {
        ArrayList<String> months = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            months.add(toMonthString(today.minusMonths(i)));
        }
        return months;
    }

    public static ArrayList<String> monthsThisYear() {
        int year = today.getYear();
        ArrayList<String> months = new ArrayList<>();
        for (Month month : Month.values()) {
            YearMonth yearMonth = YearMonth.of(year, month);
            months.add(yearMonth.format(YEAR_MONTH_FORMATTER));
        }
        return months;
    }

    public static String thisQuarter() {
        return today.format(YEAR_QUARTER_FORMATTER);
    }

    public static String lastQuarter() {
        return today.minusMonths(3).format(YEAR_QUARTER_FORMATTER);
    }

    public static ArrayList<String> lastFourQuarters() {
        ArrayList<String> quarters = new ArrayList<>();
        for (int quarter = 1; quarter <= 4; quarter++) {
            LocalDate date = today.minusMonths(quarter * 3L);
            quarters.add(today.minusMonths(quarter * 3L).format(YEAR_QUARTER_FORMATTER));
        }
        return quarters;
    }

    public static ArrayList<String> quartersThisYear() {
        int year = today.getYear();
        ArrayList<String> quarters = new ArrayList<>();
        for (int quarter = 1; quarter <= 4; quarter++) {
            quarters.add(String.format("%dQ%d", year, quarter));
        }
        return quarters;
    }

    public static int thisYear() {
        return today.getYear();
    }

    public static int lastYear() {
        return today.getYear() - 1;
    }

    public static String toDateString(String relativeDate) {
        return today.minusMonths(numberOfMonthsAgo(relativeDate)).toString();
    }

    public static String toMonthString(LocalDate date) {
        return date.format(YEAR_MONTH_FORMATTER);
    }

    public static String toMonthString(String relativeDate) {
        return LocalDate.parse(toDateString(relativeDate)).format(YEAR_MONTH_FORMATTER);
    }

    public static String toQuarterString(LocalDate date) {
        return date.format(YEAR_QUARTER_FORMATTER);
    }

    public static String toQuarterString(String relativeDate) {
       return today.minusMonths(numberOfMonthsAgo(relativeDate)).format(YEAR_QUARTER_FORMATTER);
    }

    private static Integer numberOfMonthsAgo(String relativeDate) {
        if (relativeDate.equals("thisMonth") || relativeDate.equals("thisQuarter"))
            return 0;
        else {
            String[] relativeDateArray = relativeDate.split("_");
            if (relativeDateArray[1].equals("MonthsAgo"))
                return Integer.parseInt(relativeDateArray[0]);
            else
                return 3 * Integer.parseInt(relativeDateArray[0]);
        }
    }
}
