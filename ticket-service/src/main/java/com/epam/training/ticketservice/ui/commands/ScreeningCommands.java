package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.screening.utils.DateTimeConverter;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommands {
    private final UserService userService;
    private final ScreeningService screeningService;
    private final DateTimeConverter dateTimeConverter;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Admin user can create a screening")
    public String createScreening(String movieName, String roomName, String screeningTime) {
        LocalDateTime localDateTime = dateTimeConverter.convertStringToLocalTime(screeningTime);

        return screeningService.createScreening(movieName, roomName, localDateTime);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Admin user can delete an already existing screening")
    public void deleteScreening(String movieName, String roomName, String screeningTime) {
        LocalDateTime time = dateTimeConverter.convertStringToLocalTime(screeningTime);
        screeningService.deleteScreening(movieName, roomName, time);
    }

    @ShellMethod(key = "list screenings", value = "Listing all the screenings")
    public String listScreenings() {
        List<ScreeningDto> everyScreening = screeningService.listScreenings();

        if (everyScreening.isEmpty()) {
            return "There are no screenings";
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < everyScreening.size(); ++i) {
            stringBuilder.append(everyScreening.get(i).getMovieAttributes().getMovieTitle()
                    + " ("
                    + everyScreening.get(i).getMovieAttributes().getMovieType()
                    + ", "
                    + everyScreening.get(i).getMovieAttributes().getMovieLength()
                    + " minutes), screened in room "
                    + everyScreening.get(i).getRoomAttributes().getRoomName()
                    + ", at "
                    + dateTimeConverter.convertLocalTimeToString(everyScreening.get(i).getCurrentTime()));
        }

        return stringBuilder.toString();

    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();

        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        } else {
            return Availability.unavailable("Only an Admin has the authority for these types of commands");
        }
    }

}
