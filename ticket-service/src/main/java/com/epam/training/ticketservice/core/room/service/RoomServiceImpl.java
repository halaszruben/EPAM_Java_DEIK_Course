package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.room.model.RoomDto;
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
    public void createRoom(RoomDto roomDto) {
        Room room = new Room(roomDto.getRoomName(),
                roomDto.getRoomRowOfChairs(),
                roomDto.getRoomChairPosts());

        roomRepository.save(room);
    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        Optional<Room> room = roomRepository.findRoomByRoomName(roomDto.getRoomName());

        if (room.isPresent()) {
            Room updatedRoom = room.get();
            updatedRoom.setRoomRowOfChairs(roomDto.getRoomRowOfChairs());
            updatedRoom.setRoomChairPosts(roomDto.getRoomChairPosts());

            roomRepository.save(updatedRoom);
        }
    }

    @Transactional
    @Override
    public void deleteRoom(String roomName) {
        roomRepository.deleteRoomByRoomName(roomName);
    }

    @Override
    public List<RoomDto> listRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private RoomDto convertEntityToDto(Room room) {
        return RoomDto.builder()
                .withRoomName(room.getRoomName())
                .withRoomRowOfChairs(room.getRoomRowOfChairs())
                .withRoomChairPosts(room.getRoomChairPosts())
                .build();
    }
}
