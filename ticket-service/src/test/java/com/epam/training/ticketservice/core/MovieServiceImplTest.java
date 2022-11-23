package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    void testCreateMovieShouldStoreTheMovieWhenTheInputsAreValid() {
        //Given


    }

}
