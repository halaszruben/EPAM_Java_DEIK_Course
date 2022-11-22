package com.epam.training.ticketservice.core.screening.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeConverter {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public String convertLocalTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }

    public LocalDateTime convertStringToLocalTime(String time) {
        return LocalDateTime.parse(time, dateTimeFormatter);
    }

}
