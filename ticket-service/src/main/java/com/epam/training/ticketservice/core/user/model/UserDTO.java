package com.epam.training.ticketservice.core.user.model;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.Value;

@Value
public class UserDTO {

    private final String userName;
    private final User.Role role;
}
