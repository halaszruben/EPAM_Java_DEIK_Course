package com.epam.training.ticketservice.core.room.model;

import lombok.Value;

@Value
public class RoomDTO {

    private final String roomName;
    private final int roomRowOfChairs;
    private final int roomChairPosts;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String roomName;
        private int roomRowOfChairs;
        private int roomChairPosts;

        public Builder withRoomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder roomRowOfChairs(int roomRowOfChairs) {
            this.roomRowOfChairs = roomRowOfChairs;
            return this;
        }

        public Builder roomChairPosts(int roomChairPosts) {
            this.roomChairPosts = roomChairPosts;
            return this;
        }

        public RoomDTO build() {
            return new RoomDTO(roomName, roomRowOfChairs, roomChairPosts);
        }

    }
}
