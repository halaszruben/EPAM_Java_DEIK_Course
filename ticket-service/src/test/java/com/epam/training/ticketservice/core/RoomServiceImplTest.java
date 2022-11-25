package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.room.service.RoomServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class RoomServiceImplTest {

    private static final Room ENTITY = new Room("Nagy", 10, 5);
    private static final RoomDto DTO = new RoomDto.Builder()
            .withRoomName("Nagy")
            .withRoomRowOfChairs(10)
            .withRoomChairPosts(5)
            .build();

    private final RoomRepository roomRepository = Mockito.mock(RoomRepository.class);
    private final RoomServiceImpl underTesting = new RoomServiceImpl(roomRepository);

    @Test
    void testCreateRoomShouldStoreTheRoomWhenTheInputsAreValid() {
        //Given
        Mockito.when(roomRepository.save(ENTITY)).thenReturn(ENTITY);

        //Mockito.when
        underTesting.createRoom(DTO);

        //Then
        Mockito.verify(roomRepository).save(ENTITY);
    }

    @Test
    void testListRoomsShouldReturnWithTwoElements() {
        //Given
        Mockito.when(roomRepository.findAll()).thenReturn(List.of(ENTITY));
        List<RoomDto> expected = List.of(DTO);

        //Mockito.when
        List<RoomDto> actual = underTesting.listRooms();

        //Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(roomRepository).findAll();
    }

    @Test
    void testUpdateRoomShouldUpdateTheRoomWhenListIsNotEmpty() {
        //Given
        Mockito.when(roomRepository.save(ENTITY)).thenReturn(ENTITY);
        Mockito.when(roomRepository.findRoomByRoomName(ENTITY.getRoomName())).thenReturn(Optional.of(ENTITY));

        //Mockito.when
        underTesting.updateRoom(DTO);

        //Then
        Mockito.verify(roomRepository).findRoomByRoomName(ENTITY.getRoomName());
        Mockito.verify(roomRepository).save(ENTITY);
    }

    @Test
    void testUpdateRoomShouldNotUpdateTheRoomWhenListIsEmpty() {
        //Given
        Mockito.when(roomRepository.save(ENTITY)).thenReturn(ENTITY);

        //Mockito.when, then
        underTesting.updateRoom(DTO);
    }

    @Test
    void testDeleteRoomWhenRoomTitleIsGiven() {
        //Given, When, Then
        underTesting.deleteRoom(DTO.getRoomName());
    }
}
