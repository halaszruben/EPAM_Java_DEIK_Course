package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public void createMovie(MovieDto movieDto) {
        Movie movie = new Movie(movieDto.getMovieTitle(),
                movieDto.getMovieType(),
                movieDto.getMovieLength());

        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(MovieDto movieDto) {
        Optional<Movie> movie = movieRepository.findMovieByMovieTitle(movieDto.getMovieTitle());

        if (movie.isPresent()) {
            Movie updatedMovie = movie.get();
            updatedMovie.setMovieType(movieDto.getMovieType());
            updatedMovie.setMovieLength(movieDto.getMovieLength());

            movieRepository.save(updatedMovie);
        }
    }

    @Transactional
    @Override
    public void deleteMovie(String movieTitle) {
        movieRepository.deleteMovieByMovieTitle(movieTitle);
    }

    @Override
    public List<MovieDto> listMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public int lengthOfTheMovie(String movieName) {
        Optional<MovieDto> movie = listMovies().stream()
                .filter(movieDto -> movieDto.getMovieTitle().equals(movieName))
                .findFirst();

        if (movie.isEmpty()) {
            return 0;
        }

        return movie.get().getMovieLength();
    }

    private MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
                .withMovieTitle(movie.getMovieTitle())
                .withMovieType(movie.getMovieType())
                .withMovieLength(movie.getMovieLength())
                .build();
    }

    private Optional<MovieDto> convertEntityToDto(Optional<Movie> movie) {
        return movie.isEmpty() ? Optional.empty()
                : Optional.of(convertEntityToDto(movie.get()));
    }
}
