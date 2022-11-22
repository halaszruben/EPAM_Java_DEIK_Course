package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.user.model.UserDto;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> singIn(String userName, String password);

    Optional<UserDto> singOut();

    Optional<UserDto> describe();

}
