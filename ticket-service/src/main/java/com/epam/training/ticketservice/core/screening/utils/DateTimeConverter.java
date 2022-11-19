package com.epam.training.ticketservice.core.screening.utils;

import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DateTimeConverter {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String convertLocalTimeToString(LocalDateTime localDateTime) throws DateTimeException {
        return localDateTime.format(dateTimeFormatter);
    }

    public static LocalDateTime convertStringToLocalTime(String time) throws DateTimeParseException {
        return LocalDateTime.parse(time, dateTimeFormatter);
    }

}
