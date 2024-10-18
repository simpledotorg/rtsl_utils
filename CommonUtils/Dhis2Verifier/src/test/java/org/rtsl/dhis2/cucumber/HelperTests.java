package org.rtsl.dhis2.cucumber.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelperTests {
    @Test
    public void testToISODateTimeString() throws Exception {
        String actual = Helper.toISODateTimeString("2021-01-01");

        Assertions.assertEquals("2021-01-01T00:00:00.000", actual);
    }

    @Test
    public void testToDateTime() throws Exception {
        var actual = Helper.toDateTime("2021-01-01T00:00:00.000");

        Assertions.assertEquals(2021, actual.getYear());
        Assertions.assertEquals(1, actual.getMonthValue());
        Assertions.assertEquals(1, actual.getDayOfMonth());
    }
}
