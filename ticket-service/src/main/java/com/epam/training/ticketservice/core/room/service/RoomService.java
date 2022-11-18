package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.room.model.RoomDTO;

import java.util.List;

public interface RoomService {

    void createRoom(RoomDTO roomDTO);

    void updateRoom(RoomDTO roomDTO);

    void deleteMovie(String roomName);

    List<RoomDTO> listRooms();
}
