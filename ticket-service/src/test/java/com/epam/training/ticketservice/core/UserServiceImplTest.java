package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import com.epam.training.ticketservice.core.user.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class UserServiceImplTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserServiceImpl underTesting = new UserServiceImpl(userRepository);
    private final User user = new User("admin", "admin", User.Role.ADMIN);

    @Test
    void testSignInShouldSetLoggedInUserWhenUsernameAndPasswordAreCorrect() {
        //Given
        Optional<User> expected = Optional.of(user);
        Mockito.when(userRepository.findByUserNameAndPassword("admin", "admin")).thenReturn(Optional.of(user));

        //When
        Optional<UserDto> actualUser = underTesting.singIn("admin", "admin");

        //Then
        Assertions.assertEquals(expected.get().getUserName(), actualUser.get().getUserName());
        Assertions.assertEquals(expected.get().getRole(), actualUser.get().getRole());
        Mockito.verify(userRepository).findByUserNameAndPassword("admin", "admin");
    }

    @Test
    void testSignInShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        // Given
        Optional<UserDto> expected = Optional.empty();
        Mockito.when(userRepository.findByUserNameAndPassword("dummy", "dummy")).thenReturn(Optional.empty());

        // When
        Optional<UserDto> actual = underTesting.singIn("dummy", "dummy");

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(userRepository).findByUserNameAndPassword("dummy", "dummy");
    }

    @Test
    void testSignOutShouldReturnThePreviouslySignedInUserWhenThereIsALoggedInUser() {
        //Given
        Mockito.when(userRepository.findByUserNameAndPassword("admin", "admin")).thenReturn(Optional.of(user));
        Optional<UserDto> expected = underTesting.singIn("admin", "admin");

        //When
        Optional<UserDto> actual = underTesting.singOut();

        //Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDescribeShouldReturnOptionalEmptyWhenThereIsNoOneLoggedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTesting.describe();

        // Then
        Assertions.assertEquals(expected, actual);
    }

}
