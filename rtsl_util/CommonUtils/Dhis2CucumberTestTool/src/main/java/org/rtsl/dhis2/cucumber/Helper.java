package org.rtsl.dhis2.cucumber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rtsl.dhis2.cucumber.factories.OrganisationUnit;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {
    private static final String PROGRAM_CONSTANT_FILENAME = "programConstants.json";
    public static final JsonNode PROGRAM_CONSTANTS = getProgramConstants();

    public static JsonNode getProgramConstants() {
        try {
            InputStream resource = OrganisationUnit.class.getClassLoader().getResourceAsStream(PROGRAM_CONSTANT_FILENAME);
            return new ObjectMapper().readTree(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static String toISODateTimeString(String dateString, DateTimeFormatter dateFormatter) throws Exception {
        LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
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
