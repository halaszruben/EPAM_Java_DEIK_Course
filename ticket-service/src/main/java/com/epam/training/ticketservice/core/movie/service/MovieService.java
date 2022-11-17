package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.movie.model.MovieDTO;

import java.util.List;

public interface MovieService {

    void createMovie(MovieDTO movieDTO);

    void updateMovie(MovieDTO movieDTO);

    void deleteMovie(String movieTitle);

    List<MovieDTO> listMovies();
}
