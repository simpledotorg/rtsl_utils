package org.rtsl.dhis2.cucumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {

    public static String DATE_FORMATTER = "yyyy-MM-dd";
    public static String convertToISODateTimeString(String dateString, String dateFormatter) throws Exception{
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(dateFormatter);
        LocalDate date = LocalDate.parse(dateString, inputFormatter);
        LocalDateTime dateTime = date.atStartOfDay();
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return dateTime.format(isoFormatter);
    }

    public static String convertToISODateTimeString(String dateString) throws Exception{
        return convertToISODateTimeString(dateString, DATE_FORMATTER);
    }

}
