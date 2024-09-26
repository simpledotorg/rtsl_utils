package org.rtsl.dhis2.cucumber.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rtsl.dhis2.cucumber.factories.OrganisationUnit;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static String toISODateTimeString(String dateString, DateTimeFormatter dateFormatter) throws Exception {
        LocalDate date = LocalDate.parse(dateString, dateFormatter);
        LocalDateTime dateTime = date.atStartOfDay();

        return dateTime.format(ISO_DATE_TIME_FORMATTER);
    }

    public static String toISODateTimeString(String dateString) throws Exception {
        return toISODateTimeString(dateString, DATE_FORMATTER);
    }

    public static LocalDateTime current() throws Exception {
        return LocalDateTime.now();
    }

    public static LocalDateTime toDateTime(String dateTimeString) throws Exception {
        return LocalDateTime.parse(dateTimeString, ISO_DATE_TIME_FORMATTER);
    }
}
