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
import com.epam.training.ticketservice.core.screening.utils.DateTimeChecker;
import com.epam.training.ticketservice.core.screening.utils.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final DateTimeChecker dateTimeChecker;
    private final DateTimeConverter dateTimeConverter;


    @Override
    public String createScreening(String movieName, String roomName, LocalDateTime screeningStartTimer) {
        Optional<Movie> movieAttrExists = movieRepository.findMovieByMovieTitle(movieName);
        Optional<Room> roomAttrExists = roomRepository.findRoomByRoomName(roomName);

        String screeningTime = dateTimeConverter.convertLocalTimeToString(screeningStartTimer);

        var screeningsForRooms = getRoomScreenings(roomName);

        if (screeningsForRooms.isEmpty()) {
            Screening screening = new Screening();
            screening.setMovieId(movieAttrExists.get().getId());
            screening.setRoomId(roomAttrExists.get().getId());
            screening.setScreeningStartTimer(screeningStartTimer);
            this.screeningRepository.save(screening);

            return "The screening has been saved successfully";
        }

        if (movieAttrExists.isEmpty() || roomAttrExists.isEmpty()) {
            return "One or both of the inputs are wrong";
        }

        if (!dateTimeChecker.validScreeningIntervals(movieName,
                screeningTime, screeningsForRooms) && !screeningsForRooms.isEmpty()) {
            return "There is an overlapping screening";
        }

        if (!dateTimeChecker.enoughMinutesBetweenScreens(roomName,
                screeningTime, screeningsForRooms) && !screeningsForRooms.isEmpty()) {
            return "This would start in the break period after another screening in this room";
        }


        Screening screening = new Screening(movieAttrExists.get().getId(),
                roomAttrExists.get().getId(), screeningStartTimer);

        screeningRepository.save(screening);

        return "The screening has been saved successfully";
    }


    @Transactional
    @Override
    public void deleteScreening(String movieName, String roomName, LocalDateTime screeningTime) {
        Optional<Movie> movieAttrExists = movieRepository.findMovieByMovieTitle(movieName);
        Optional<Room> roomAttrExists = roomRepository.findRoomByRoomName(roomName);

        screeningRepository.deleteScreeningByMovieIdAndRoomIdAndScreeningStartTimer(
                movieAttrExists.get().getId(),
                roomAttrExists.get().getId(),
                screeningTime
        );
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

    public List<ScreeningDto> getRoomScreenings(String roomName) {
        return this.listScreenings()
                .stream()
                .filter(screeningDTO -> screeningDTO.getRoomAttributes().getRoomName().equals(roomName))
                .collect(Collectors.toList());
    }

}
