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
import com.github.mawippel.validator.OverlappingVerificator;
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


    @Override
    public String createScreening(String movieName, String roomName, LocalDateTime screeningStartTimer) {
        Optional<Movie> movieAttrExists = movieRepository.findMovieByMovieTitle(movieName);
        Optional<Room> roomAttrExists = roomRepository.findRoomByRoomName(roomName);

        var screeningsForRooms = getRoomScreenings(roomName);

        if (movieAttrExists.isEmpty() || roomAttrExists.isEmpty()) {
            return "One or both of the inputs are wrong";
        }

        for (Screening screening : screeningRepository.findAll()) {
            if (OverlappingVerificator.isOverlap(screeningStartTimer,
                    screeningStartTimer.plusMinutes(movieAttrExists.get().getMovieLength()),
                    screening.getScreeningStartTimer(),
                    screening.getScreeningStartTimer().plusMinutes(
                            movieRepository.findById(screening.getMovieId()).get().getMovieLength()))) {
                return "There is an overlapping screening";
            } else if (OverlappingVerificator.isOverlap(screeningStartTimer,
                    screeningStartTimer.plusMinutes(movieAttrExists.get().getMovieLength() + 9),
                    screening.getScreeningStartTimer(),
                    screening.getScreeningStartTimer()
                            .plusMinutes(movieRepository.findById(screening.getMovieId()).get().getMovieLength()
                                    + 10))) {
                return "This would start in the break period after another screening in this room";
            }
        }

        if (screeningsForRooms.isEmpty()) {
            Screening screening = new Screening();
            screening.setMovieId(movieAttrExists.get().getId());
            screening.setRoomId(roomAttrExists.get().getId());
            screening.setScreeningStartTimer(screeningStartTimer);
            this.screeningRepository.save(screening);

            return "The screening has been saved successfully";
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

    public List<ScreeningDto> getRoomScreenings(String roomName) {
        return this.listScreenings()
                .stream()
                .filter(screeningDTO -> screeningDTO.getRoomAttributes().getRoomName().equals(roomName))
                .collect(Collectors.toList());
    }
}
