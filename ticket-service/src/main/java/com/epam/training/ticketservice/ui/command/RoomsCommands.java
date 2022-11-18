package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.model.RoomDTO;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.user.model.UserDTO;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class RoomsCommands {

    private final RoomService roomService;
    private final UserService userService;


    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Admin user can create a room")
    public void createRoom(String roomName, Integer roomRowOfChairs, Integer roomChairPosts) {
        RoomDTO newRoom = RoomDTO.builder()
                .withRoomName(roomName)
                .roomRowOfChairs(roomRowOfChairs)
                .roomChairPosts(roomChairPosts)
                .build();

        roomService.createRoom(newRoom);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Admin user can update an already existing room")
    public void updateRoom(String roomName, Integer roomRowOfChairs, Integer roomChairPosts) {
        RoomDTO roomToUpdate = RoomDTO.builder()
                .withRoomName(roomName)
                .roomRowOfChairs(roomRowOfChairs)
                .roomChairPosts(roomChairPosts)
                .build();

        roomService.updateRoom(roomToUpdate);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Admin user can delete an already existing room")
    public void deleteRoom(String roomName) {
        roomService.deleteMovie(roomName);
    }

    @ShellMethod(key = "list rooms", value = "Listing all the rooms")
    public String listRooms() {
        List<RoomDTO> everyRoom = roomService.listRooms();

        if (everyRoom.isEmpty()){
            return "There are no rooms at the moment";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (RoomDTO room : everyRoom){
            stringBuilder.append("Room "
            + room.getRoomName()
            + " with "
            + room.getRoomRowOfChairs() * room.getRoomChairPosts()
            + " seats, "
            + room.getRoomRowOfChairs()
            + " rows and "
            + room.getRoomChairPosts()
            + " columns\n");
        }

        return stringBuilder.toString();
    }

    private Availability isAvailable() {
        Optional<UserDTO> user = userService.describe();

        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        } else
            return Availability.unavailable("Only an Admin has the authority for these types of commands");
    }
}
