package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.movie.service.MovieServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    private static final Movie ENTITY = new Movie("Avatar", "Sci-fi", 126);
    private static final MovieDto DTO = new MovieDto.Builder()
            .withMovieTitle("Avatar")
            .withMovieType("Sci-fi")
            .withMovieLength(126)
            .build();

    private final MovieRepository movieRepository = Mockito.mock(MovieRepository.class);
    private final MovieServiceImpl underTesting = new MovieServiceImpl(movieRepository);

    @Test
    void testCreateMovieShouldStoreTheMovieWhenTheInputsAreValid() {
        //Given
        Mockito.when(movieRepository.save(ENTITY)).thenReturn(ENTITY);

        //Mockito.when
        underTesting.createMovie(DTO);

        //Then
        Mockito.verify(movieRepository).save(ENTITY);
    }

    @Test
    void testListMoviesShouldReturnWithTwoElements() {
        //Given
        Mockito.when(movieRepository.findAll()).thenReturn(List.of(ENTITY));
        List<MovieDto> expected = List.of(DTO);

        //Mockito.when
        List<MovieDto> actual = underTesting.listMovies();

        //Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(movieRepository).findAll();
    }

    @Test
    void testUpdateMovieShouldUpdateTheMovieWhenListIsNotEmpty() {
        //Given
        Mockito.when(movieRepository.save(ENTITY)).thenReturn(ENTITY);
        Mockito.when(movieRepository.findMovieByMovieTitle(ENTITY.getMovieTitle())).thenReturn(Optional.of(ENTITY));

        //Mockito.when
        underTesting.updateMovie(DTO);

        //Then
        Mockito.verify(movieRepository).findMovieByMovieTitle(ENTITY.getMovieTitle());
        Mockito.verify(movieRepository).save(ENTITY);
    }

    @Test
    void testUpdateMovieShouldNotUpdateTheMovieWhenListIsEmpty() {
        //Given
        Mockito.when(movieRepository.save(ENTITY)).thenReturn(ENTITY);

        //Mockito.when, then
        underTesting.updateMovie(DTO);
    }

    @Test
    void testDeleteMovieWhenMovieTitleIsGiven() {
        //Given, When, Then
        underTesting.deleteMovie(DTO.getMovieTitle());
    }

    @Test
    void testLengthOfTheMovieShouldReturnNullWhenThereAreNoMovies() {
        //Given, When
        int actual = underTesting.lengthOfTheMovie(DTO.getMovieTitle());

        //Then
        Assertions.assertEquals(0, actual);
    }

    @Test
    void testLengthOfTheMovieShouldReturnWithTheLengthOfTheMovieWhenThereIsAMovie() {
        //Given
        Mockito.when(movieRepository.findAll()).thenReturn(List.of(ENTITY));
        List<MovieDto> listMovies = List.of(DTO);
        int expected = listMovies.get(0).getMovieLength();

        //Mockito.when
        int actual = underTesting.lengthOfTheMovie(DTO.getMovieTitle());

        //Then
        Assertions.assertEquals(expected,actual);
        Mockito.verify(movieRepository).findAll();
    }

}
