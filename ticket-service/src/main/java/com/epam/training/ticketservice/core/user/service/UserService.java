package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.user.model.UserDTO;

import java.util.Optional;

public interface UserService {

    Optional<UserDTO> singIn(String userName, String password);

    Optional<UserDTO> singOut();

    Optional<UserDTO> describe();

}
