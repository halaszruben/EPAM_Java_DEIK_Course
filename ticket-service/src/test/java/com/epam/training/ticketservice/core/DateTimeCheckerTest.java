package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.utils.DateTimeChecker;
import com.epam.training.ticketservice.core.screening.utils.DateTimeConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class DateTimeCheckerTest {

    private static final Movie movieENTITY = new Movie("Avatar", "Sci-fi", 126);

    private static final MovieDto movieDTO = new MovieDto.Builder()
            .withMovieTitle("Avatar")
            .withMovieType("Sci-fi")
            .withMovieLength(126)
            .build();

    private static final ScreeningDto screeningDTO = new ScreeningDto.Builder().build();

    private static final String movieName = "dummy";
    private static final String dateTime = "invalid";

    private static final List<ScreeningDto> screeningListDTO = List.of(screeningDTO, screeningDTO, screeningDTO, screeningDTO);

    @InjectMocks
    private DateTimeChecker underTesting;
    @Mock
    private MovieService movieService;
    @Mock
    private DateTimeConverter dateTimeConverter;


    @ParameterizedTest
    @MethodSource("invalidScreeningData")
    void testValidScreeningIntervals(LocalDateTime st, Integer lengthST, LocalDateTime nd, Integer lengthND) {
        //Given
        boolean expected = false;
        Mockito.when(dateTimeConverter.convertStringToLocalTime(Mockito.any())).thenReturn(st).thenReturn(nd);
        Mockito.when(movieService.lengthOfTheMovie(Mockito.any())).thenReturn(lengthST).thenReturn(lengthND);

        //When
        boolean actual = underTesting.enoughMinutesBetweenScreens(movieName, dateTime, screeningListDTO);

        //Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(dateTimeConverter).convertStringToLocalTime(Mockito.any());
        Mockito.verify(movieService).lengthOfTheMovie(Mockito.any());
    }

    public static Stream<Arguments> invalidScreeningData() {

        LocalDateTime st = LocalDateTime.of(2022, 12, 1, 10, 0);
        int lengthST = 126;

        return Stream.of(Arguments.of(st, lengthST,
                        LocalDateTime.of(2022, 12, 1, 10, 10), 60)
                , Arguments.of(st, lengthST,
                        LocalDateTime.of(2022, 12, 1, 9, 0), 240)
                , Arguments.of(st, lengthST,
                        LocalDateTime.of(2022, 12, 1, 9, 0), 120)
                , Arguments.of(st, lengthST,
                        LocalDateTime.of(2022, 12, 1, 11, 0), 120)
        );
    }
}
