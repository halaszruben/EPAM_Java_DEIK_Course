package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.model.MovieDTO;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.core.user.model.UserDTO;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class MoviesCommands {

    private final MovieService movieService;
    private final UserService userService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Admin user can create movie")
    public void createMovies(String movieTitle, String movieType, Integer movieLength) {
        MovieDTO newMovie = MovieDTO.builder()
                .withMovieTitle(movieTitle)
                .withMovieType(movieType)
                .withMovieLength(movieLength)
                .build();

        movieService.createMovie(newMovie);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Admin user can update an already existing movie")
    public void updateMovie(String movieTitle, String movieType, Integer movieLength) {
        MovieDTO movieToUpdate = MovieDTO.builder()
                .withMovieTitle(movieTitle)
                .withMovieType(movieType)
                .withMovieLength(movieLength)
                .build();

        movieService.updateMovie(movieToUpdate);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Admin user can delete an already existing movie")
    public void deleteMovie(String movieTitle) {
        movieService.deleteMovie(movieTitle);
    }

    @ShellMethod(key = "list movies", value = "Listing all the movies")
    public String listMovies() {
        List<MovieDTO> everyMovie = movieService.listMovies();

        if (everyMovie.isEmpty()) {
            return "There are no movies at the moment";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (MovieDTO movie : everyMovie) {
            stringBuilder.append(movie.getMovieTitle()
            + " ("
            + movie.getMovieType()
            + ", "
            + movie.getMovieLength()
            + " minutes)\n");
        }

        return stringBuilder.toString();
    }

    private Availability isAvailable() {
        Optional<UserDTO> user = userService.describe();

        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        } else
            return Availability.unavailable("Only an Admin has the authority for these types of commands");
    }

}
