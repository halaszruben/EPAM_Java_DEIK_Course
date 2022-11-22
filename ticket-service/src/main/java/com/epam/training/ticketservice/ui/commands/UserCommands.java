package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class UserCommands {

    private final UserService userService;

    @ShellMethod(key = "sign in privileged", value = "Admin User Login")
    public String login(String userName, String password) {
        Optional<UserDto> user = userService.singIn(userName, password);
        if (user.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }

        return "Welcome back " + user.get().getUserName();
    }

    @ShellMethod(key = "sign out", value = "Any User Logout")
    public String logout() {
        Optional<UserDto> user = userService.singOut();
        if (user.isEmpty()) {
            return "In order to logout, first you need to be logged in";
        }

        return "Successfully signed out";
    }

    @ShellMethod(key = "describe account", value = "Describing the Logged In User")
    public String describe() {
        Optional<UserDto> user = userService.describe();
        if (user.isEmpty()) {
            return "You are not signed in";
        }

        return "Signed in with privileged account '" + user.get().getUserName() + "'";
    }
}
