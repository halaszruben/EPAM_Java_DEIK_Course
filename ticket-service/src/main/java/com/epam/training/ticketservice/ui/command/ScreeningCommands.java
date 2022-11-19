package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.screening.utils.DateTimeChecks;
import com.epam.training.ticketservice.core.screening.utils.DateTimeConverter;
import com.epam.training.ticketservice.core.user.model.UserDTO;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommands {
    private final UserService userService;
    private final ScreeningService screeningService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Admin user can create a screening")
    public void createScreening(String movieName, String roomName, String screeningTime) {
        LocalDateTime localDateTime = DateTimeConverter.convertStringToLocalTime(screeningTime);

        screeningService.createScreening(movieName, roomName, localDateTime);
    }

    private Availability isAvailable() {
        Optional<UserDTO> user = userService.describe();

        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        } else
            return Availability.unavailable("Only an Admin has the authority for these types of commands");
    }

}
