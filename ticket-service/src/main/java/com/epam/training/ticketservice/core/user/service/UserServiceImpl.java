package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.user.model.UserDTO;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private UserDTO signInUser = null;

    @Override
    public Optional<UserDTO> singIn(String userName, String password) {
        Optional<User> user = userRepository.findByUserNameAndPassword(userName, password);
        if (user.isEmpty())
            return Optional.empty();

        signInUser = new UserDTO(user.get().getUserName(), user.get().getRole());

        return describe();
    }

    @Override
    public Optional<UserDTO> singOut() {
        Optional<UserDTO> previouslySingInUser = describe();
        signInUser = null;

        return previouslySingInUser;
    }

    @Override
    public Optional<UserDTO> describe() {
        return Optional.ofNullable(signInUser);
    }

}
