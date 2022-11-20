package com.epam.training.ticketservice.core.screening.utils;

import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DateTimeChecker {

    private final MovieService movieService;

    private boolean overlappingBetweenMovies(LocalDateTime startMovieOne, LocalDateTime endMovieOne, LocalDateTime startMovieTwo, LocalDateTime endMovieTwo) {

        if (startMovieOne.isBefore(startMovieTwo) && endMovieOne.isAfter(startMovieTwo)) {
            return true;
        } else if (startMovieOne.isBefore(endMovieTwo) && endMovieOne.isAfter(endMovieTwo)) {
            return true;
        } else if (startMovieOne.isBefore(startMovieTwo) && endMovieOne.isAfter(endMovieTwo)) {
            return true;
        } else return startMovieOne.isAfter(startMovieTwo) && endMovieOne.isBefore(endMovieTwo);
    }

    private boolean enoughMinutesBetweenBrakes(LocalDateTime startMovieOne, LocalDateTime endMovieOne, LocalDateTime startMovieTwo, LocalDateTime endMovieTwo) {
        int minimBrakeTime = 10;
        if (startMovieOne.isBefore(startMovieTwo)
                && endMovieOne.isBefore(startMovieTwo)
                && breakTime(endMovieOne, startMovieTwo) <= minimBrakeTime) {
            return false;
        } else if (startMovieTwo.isBefore(startMovieOne)
                && endMovieTwo.isBefore(startMovieOne)
                && breakTime(endMovieTwo, startMovieOne) <= minimBrakeTime) {
            return false;
        } else return !endMovieOne.equals(startMovieTwo) && !endMovieTwo.equals(startMovieOne);
    }

    private long breakTime(LocalDateTime fromDate, LocalDateTime toDate) {
        return ChronoUnit.MINUTES.between(fromDate, toDate);
    }

    private LocalDateTime endOfTheScreening(String movieName, LocalDateTime movieStartTimer) {
        int movieLength = movieService.lengthOfTheMovie(movieName);

        return movieStartTimer.plusMinutes(movieLength);
    }

    public boolean validScreeningIntervals(String movieName, String dateTime, List<ScreeningDto> currentScreeningsInRoom) {
        LocalDateTime startOfNewScreen = DateTimeConverter.convertStringToLocalTime(dateTime);
        LocalDateTime endOfNewScreen = endOfTheScreening(movieName, startOfNewScreen);

        for (var screenings : currentScreeningsInRoom) {
            LocalDateTime newScreenStart = screenings.getCurrentTime();
            LocalDateTime newScreenEnd = endOfTheScreening(screenings.getMovieAttributes().getMovieTitle(), newScreenStart);

            if (!overlappingBetweenMovies(startOfNewScreen, endOfNewScreen, newScreenStart, newScreenEnd)) {
                return true;
            }
        }

        return false;
    }

    public boolean thereAreEnoughMinutesBetweenScreenings(String movieName, String screeningTime
            , List<ScreeningDto> currentScreeningsInRoom) {
        LocalDateTime startOfNewScreen = DateTimeConverter.convertStringToLocalTime(screeningTime);
        LocalDateTime endOfNewScreen = endOfTheScreening(movieName, startOfNewScreen);

        for (var screenings : currentScreeningsInRoom) {
            LocalDateTime newScreenStart = screenings.getCurrentTime();
            LocalDateTime newScreenEnd = endOfTheScreening(screenings.getMovieAttributes().getMovieTitle(), newScreenStart);

            if (!enoughMinutesBetweenBrakes(startOfNewScreen, endOfNewScreen, newScreenStart, newScreenEnd)) {
                return false;
            }
        }

        return true;
    }
}
