package org.rtsl.dhis2.cucumber;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PeriodTests {
    @Test
    public void testMonthsAgo() throws Exception {
        assertMonthsBetween("3_MonthsAgo", -3);
    }

    @Test
    public void testThisMonth() throws Exception {
        assertMonthsBetween("thisMonth", 0);
    }

    @Test
    public void testQuartersAgo() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate pointInTime = Period.parseRelativeDate("2_QuartersAgo");
        long diff = ChronoUnit.MONTHS.between(pointInTime, today);
        Assertions.assertTrue(diff >= 6); // Closer boundary
        Assertions.assertTrue(diff <= 9); // Farther boundary
    }

    @Test
    public void testThisQuarter() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate pointInTime = Period.parseRelativeDate("thisQuarter");
        long diff = ChronoUnit.MONTHS.between(pointInTime, today);
        Assertions.assertTrue(diff >= 0); // Closer boundary
        Assertions.assertTrue(diff <= 3); // Farther boundary
    }

    @Test
    public void testBeginningOfMonth() throws Exception {
        LocalDate thisMonth = Period.parseRelativeDate("thisMonth");
        LocalDate firstDayOfMonth = thisMonth.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate pointInTime = Period.parseRelativeDate("BeginningOfMonth");
        Assertions.assertEquals(firstDayOfMonth, pointInTime);
    }

    @Test
    public void testEndOfMonth() throws Exception {
        LocalDate thisMonth = Period.parseRelativeDate("thisMonth");
        LocalDate firstDayOfMonth = thisMonth.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate pointInTime = Period.parseRelativeDate("EndOfMonth");
        Assertions.assertEquals(firstDayOfMonth, pointInTime);
    }

    @Test
    public void testDaysAgo() throws Exception {
        assertDaysBetween("4_DaysAgo", -4);
    }

    @Test
    public void testPlusDays() throws Exception {
        LocalDate thisMonth = Period.parseRelativeDate("thisMonth");
        assertDaysBetweenFrom(thisMonth, "thisMonth_Plus_1_Day", 1);
    }

    @Test
    public void testPlusMonths() throws Exception {
        LocalDate thisMonth = Period.parseRelativeDate("thisMonth");
        assertMonthsBetweenFrom(thisMonth, "thisMonth_Plus_1_Month", 1);
    }

    @Test
    public void testMinusDays() throws Exception {
        LocalDate thisMonth = Period.parseRelativeDate("thisMonth");
        assertDaysBetweenFrom(thisMonth, "thisMonth_Minus_1_Day", -1);
    }

    @Test
    public void testMinusMonths() throws Exception {
        LocalDate thisMonth = Period.parseRelativeDate("thisMonth");
        assertMonthsBetweenFrom(thisMonth, "thisMonth_Minus_1_Month", -1);
    }

    private void assertMonthsBetween(String relativeDate, long expectedMonths) {
        assertTimeBetween(relativeDate, expectedMonths, ChronoUnit.MONTHS);
    }

    private void assertDaysBetween(String relativeDate, long expectedDays) {
        assertTimeBetween(relativeDate, expectedDays, ChronoUnit.DAYS);
    }

    private void assertMonthsBetweenFrom(LocalDate fixedDate, String relativeDate, long expectedMonths) {
        assertTimeBetweenFrom(fixedDate, relativeDate, expectedMonths, ChronoUnit.MONTHS);
    }

    private void assertDaysBetweenFrom(LocalDate fixedDate, String relativeDate, long expectedDays) {
        assertTimeBetweenFrom(fixedDate, relativeDate, expectedDays, ChronoUnit.DAYS);
    }

    private void assertTimeBetween(String relativeDate, long expectedMonths, ChronoUnit timeUnit) {
        LocalDate today = LocalDate.now();
        assertTimeBetweenFrom(today, relativeDate, expectedMonths, timeUnit);
    }

    private void assertTimeBetweenFrom(LocalDate fixedDate, String relativeDate, long expectedMonths, ChronoUnit timeUnit) {
        LocalDate pointInTime = Period.parseRelativeDate(relativeDate);
        long diff = timeUnit.between(fixedDate, pointInTime);
        Assertions.assertEquals(expectedMonths, diff);
    }
}
