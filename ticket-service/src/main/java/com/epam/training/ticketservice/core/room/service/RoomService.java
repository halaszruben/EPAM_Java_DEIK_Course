package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.room.model.RoomDto;

import java.util.List;

public interface RoomService {

    void createRoom(RoomDto roomDto);

    void updateRoom(RoomDto roomDto);

    void deleteRoom(String roomName);

    List<RoomDto> listRooms();
}
