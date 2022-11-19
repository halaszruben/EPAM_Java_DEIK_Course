package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.utils.DateTimeChecks;
import com.epam.training.ticketservice.core.screening.utils.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final DateTimeChecks dateTimeChecks;


    @Override
    public String createScreening(String movieName, String roomName, LocalDateTime screeningStartTimer) {
        Optional<Movie> movieAttrExists = movieRepository.findMovieByMovieTitle(movieName);
        Optional<Room> roomAttrExists = roomRepository.findRoomByRoomName(roomName);

        String screeningTime = DateTimeConverter.convertLocalTimeToString(screeningStartTimer);

        var screeningsForRooms = getRoomScreenings(roomName);

        if (movieAttrExists.isPresent() && roomAttrExists.isPresent()) {
            if (!dateTimeChecks.validScreeningIntervals(movieAttrExists.get().getMovieTitle(),
                    screeningTime, screeningsForRooms)) {
                return "There is an overlapping screening";
            }

            if (!dateTimeChecks.thereAreEnoughMinutesBetweenScreenings(movieAttrExists.get().getMovieTitle(),
                    screeningTime, screeningsForRooms)) {
                return "This would start in the break period after another screening in this room";
            }
        }

        Screening screening = new Screening(movieAttrExists.get().getId(),
                roomAttrExists.get().getId(), screeningStartTimer);

        screeningRepository.save(screening);

        return screeningRepository.toString();
    }


    @Transactional
    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
//        LocalDateTime dateOfScreening = screeningDto.getCurrentTime();
//        screeningRepository.deleteScreeningByCurrentTime(screeningDto.getCurrentTime());

    }

    @Override
    public List<ScreeningDto> listScreenings() {
        return screeningRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private ScreeningDto convertEntityToDto(Screening screening) {
        Optional<Movie> movie = movieRepository.findById(screening.getMovieId());
        Optional<Room> room = roomRepository.findById(screening.getRoomId());

        MovieDto movieAttr = new MovieDto(movie.get().getMovieTitle(),
                movie.get().getMovieType(),
                movie.get().getMovieLength());
        RoomDto roomAttr = new RoomDto(room.get().getRoomName(),
                room.get().getRoomRowOfChairs(),
                room.get().getRoomChairPosts());

        return ScreeningDto.builder()
                .withMovieAttr(movieAttr)
                .withRoomAttr(roomAttr)
                .withCurrentTime(screening.getScreeningStartTimer())
                .build();
    }

    private Optional<ScreeningDto> convertEntityToDto(Optional<Screening> screening) {
        return screening.isEmpty() ? Optional.empty()
                : Optional.of(convertEntityToDto(screening.get()));
    }

    private Optional<Long> calculatingMinutesBetweenScreens(LocalDateTime screeningTimer, Integer movieId) {
        Optional<Screening> screening = screeningRepository
                .findScreeningByMovieIdEqualsAndScreeningStartTimer(movieId, screeningTimer);
        Optional<Room> room = roomRepository.findById(screening.get().getRoomId());

        if (room.isPresent()) {
            return Optional.of(breakTime(screening.get().getScreeningStartTimer(), screeningTimer));
        }

        return Optional.empty();
    }


    private long breakTime(LocalDateTime fromDate, LocalDateTime toDate) {
        long minutes = ChronoUnit.MINUTES.between(fromDate, toDate);

        return minutes;
    }

    public List<ScreeningDto> getRoomScreenings(String roomName) {
        return this.listScreenings()
                .stream()
                .filter(screeningDTO -> screeningDTO.getRoomAttributes().getRoomName().equals(roomName))
                .collect(Collectors.toList());
    }

}
