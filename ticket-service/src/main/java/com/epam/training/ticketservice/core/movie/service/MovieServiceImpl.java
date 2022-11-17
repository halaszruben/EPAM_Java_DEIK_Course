package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.movie.model.MovieDTO;
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
    public void createMovie(MovieDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getMovieTitle(),
                movieDTO.getMovieType(),
                movieDTO.getMovieLength());
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(MovieDTO movieDTO) {
        Optional<Movie> movie = movieRepository.findMovieByMovieTitle(movieDTO.getMovieTitle());

        if (movie.isPresent()) {
            Movie updatedMovie = movie.get();
            updatedMovie.setMovieType(movieDTO.getMovieType());
            updatedMovie.setMovieLength(movieDTO.getMovieLength());

            movieRepository.save(updatedMovie);
        }
    }

    @Transactional
    @Override
    public void deleteMovie(String movieTitle) {
        movieRepository.deleteMovieByMovieTitle(movieTitle);
    }

    @Override
    public List<MovieDTO> listMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private MovieDTO convertEntityToDto(Movie movie) {
        return MovieDTO.builder()
                .withMovieTitle(movie.getMovieTitle())
                .withMovieType(movie.getMovieType())
                .withMovieLength(movie.getMovieLength())
                .build();
    }

    private Optional<MovieDTO> convertEntityToDto(Optional<Movie> movie) {
        return movie.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(movie.get()));
    }
}
