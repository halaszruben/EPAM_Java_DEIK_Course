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
    private final DateTimeConverter dateTimeConverter;

    private boolean overlapping(LocalDateTime st, LocalDateTime endSt, LocalDateTime nd, LocalDateTime endNd) {

        if (st.isBefore(nd) && endSt.isAfter(nd)) {
            return true;
        } else if (st.isBefore(endNd) && endSt.isAfter(endNd)) {
            return true;
        } else if (st.isBefore(nd) && endSt.isAfter(endNd)) {
            return true;
        } else {
            return st.isAfter(nd) && endSt.isBefore(endNd);
        }
    }

    private boolean enoughMinutesInBrake(LocalDateTime st, LocalDateTime endSt, LocalDateTime nd, LocalDateTime endNd) {
        int minimBrakeTime = 10;
        if (st.isBefore(nd)
                && endSt.isBefore(nd)
                && breakTime(endSt, nd) <= minimBrakeTime) {
            return false;
        } else if (nd.isBefore(st)
                && endNd.isBefore(st)
                && breakTime(endNd, st) <= minimBrakeTime) {
            return false;
        } else {
            return !endSt.equals(nd)
                    && !endNd.equals(st);
        }
    }

    private long breakTime(LocalDateTime fromDate, LocalDateTime toDate) {
        return ChronoUnit.MINUTES.between(fromDate, toDate);
    }

    private LocalDateTime endOfTheScreening(String movieName, LocalDateTime movieStartTimer) {
        int movieLength = movieService.lengthOfTheMovie(movieName);

        return movieStartTimer.plusMinutes(movieLength);
    }

    public boolean validScreeningIntervals(String movieName, String dateTime, List<ScreeningDto> screenings) {
        LocalDateTime startOfNewScreen = dateTimeConverter.convertStringToLocalTime(dateTime);
        LocalDateTime endOfNewScreen = endOfTheScreening(movieName, startOfNewScreen);

        for (var screening : screenings) {
            LocalDateTime newScreenStart = screening.getCurrentTime();
            LocalDateTime newScreenEnd = endOfTheScreening(screening.getMovieAttributes().getMovieTitle(),
                    newScreenStart);

            if (!overlapping(startOfNewScreen, endOfNewScreen, newScreenStart, newScreenEnd)) {
                return true;
            }
        }

        return false;
    }

    public boolean enoughMinutesBetweenScreens(String movieName, String screeningTime, List<ScreeningDto> screenings) {
        LocalDateTime startOfNewScreen = dateTimeConverter.convertStringToLocalTime(screeningTime);
        LocalDateTime endOfNewScreen = endOfTheScreening(movieName, startOfNewScreen);

        for (var screening : screenings) {
            LocalDateTime newScreenStart = screening.getCurrentTime();
            LocalDateTime newScreenEnd = endOfTheScreening(screening.getMovieAttributes().getMovieTitle(),
                    newScreenStart);

            if (!enoughMinutesInBrake(startOfNewScreen, endOfNewScreen, newScreenStart, newScreenEnd)) {
                return false;
            }
        }

        return true;
    }
}
