package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.screening.utils.DateTimeConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class DateTimeConverterTest {

    DateTimeConverter dateTimeConverter = new DateTimeConverter();

    @Test
    void testDateTimeConverterShouldReturnStringWhenLocalDateTimeIsGiven() {
        // Given
        LocalDateTime time = LocalDateTime.of(2022, 04, 11, 16, 00);

        // When
        String result = dateTimeConverter.convertLocalTimeToString(time);

        // Then
        Assertions.assertEquals("2022-04-11 16:00", result);
    }

    @Test
    public void testShouldReturnLocalDateTimeWhenParsingGivenACorrectString() {

        // Given
        String s = "2021-05-12 15:03";

        // When
        LocalDateTime result = dateTimeConverter.convertStringToLocalTime(s);

        // Then
        Assertions.assertEquals(LocalDateTime.of(2021, 5, 12, 15, 3), result);
    }
}
