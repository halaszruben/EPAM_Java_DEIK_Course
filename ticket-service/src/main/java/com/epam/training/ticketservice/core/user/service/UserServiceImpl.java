package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private UserDto signInUser = null;

    @Override
    public Optional<UserDto> singIn(String userName, String password) {
        Optional<User> user = userRepository.findByUserNameAndPassword(userName, password);
        if (user.isEmpty()) {
            return Optional.empty();
        }

        signInUser = new UserDto(user.get().getUserName(), user.get().getRole());

        return describe();
    }

    @Override
    public Optional<UserDto> singOut() {
        Optional<UserDto> previouslySingInUser = describe();
        signInUser = null;

        return previouslySingInUser;
    }

    @Override
    public Optional<UserDto> describe() {
        return Optional.ofNullable(signInUser);
    }

}
