package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.room.model.RoomDTO;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(RoomDTO roomDTO) {
        Room room = new Room(roomDTO.getRoomName(),
                roomDTO.getRoomRowOfChairs(),
                roomDTO.getRoomChairPosts());

        roomRepository.save(room);
    }

    @Override
    public void updateRoom(RoomDTO roomDTO) {
        Optional<Room> room = roomRepository.findRoomByRoomName(roomDTO.getRoomName());

        if (room.isPresent()) {
            Room updatedRoom = room.get();
            updatedRoom.setRoomRowOfChairs(roomDTO.getRoomRowOfChairs());
            updatedRoom.setRoomChairPosts(roomDTO.getRoomChairPosts());

            roomRepository.save(updatedRoom);
        }
    }

    @Transactional
    @Override
    public void deleteMovie(String roomName) {
        roomRepository.deleteRoomByRoomName(roomName);
    }

    @Override
    public List<RoomDTO> listRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private RoomDTO convertEntityToDto(Room room) {
        return RoomDTO.builder()
                .withRoomName(room.getRoomName())
                .roomRowOfChairs(room.getRoomRowOfChairs())
                .roomChairPosts(room.getRoomChairPosts())
                .build();
    }

    private Optional<RoomDTO> convertEntityToDto(Optional<Room> room) {
        return room.isEmpty() ? Optional.empty()
                : Optional.of(convertEntityToDto(room.get()));
    }

}
