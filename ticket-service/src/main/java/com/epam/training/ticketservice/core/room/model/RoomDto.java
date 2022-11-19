package com.epam.training.ticketservice.core.room.model;

import lombok.Value;

@Value
public class RoomDto {

    private final String roomName;
    private final Integer roomRowOfChairs;
    private final Integer roomChairPosts;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String roomName;
        private Integer roomRowOfChairs;
        private Integer roomChairPosts;

        public Builder withRoomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder withRoomRowOfChairs(Integer roomRowOfChairs) {
            this.roomRowOfChairs = roomRowOfChairs;
            return this;
        }

        public Builder withRoomChairPosts(Integer roomChairPosts) {
            this.roomChairPosts = roomChairPosts;
            return this;
        }

        public RoomDto build() {
            return new RoomDto(roomName, roomRowOfChairs, roomChairPosts);
        }

    }
}
