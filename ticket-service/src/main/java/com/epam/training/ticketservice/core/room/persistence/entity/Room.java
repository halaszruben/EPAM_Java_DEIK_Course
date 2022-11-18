package com.epam.training.ticketservice.core.room.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String roomName;

    private Integer roomRowOfChairs;

    private Integer roomChairPosts;

    public Room(String roomName, Integer roomRowOfChairs, Integer roomChairPosts){
        this.roomName = roomName;
        this.roomRowOfChairs = roomRowOfChairs;
        this.roomChairPosts = roomChairPosts;
    }


}
