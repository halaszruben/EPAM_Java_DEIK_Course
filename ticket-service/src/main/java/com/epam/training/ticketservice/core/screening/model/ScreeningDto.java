package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ScreeningDto {

    MovieDto movieAttributes;
    RoomDto roomAttributes;
    LocalDateTime currentTime;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private MovieDto movieAttributes;
        private RoomDto roomAttributes;
        private LocalDateTime screeningStartTimer;

        public Builder withMovieAttr(MovieDto movieAttr) {
            this.movieAttributes = movieAttr;
            return this;
        }

        public Builder withRoomAttr(RoomDto roomAttr) {
            this.roomAttributes = roomAttr;
            return this;
        }

        public Builder withCurrentTime(LocalDateTime screeningStartTimer) {
            this.screeningStartTimer = screeningStartTimer;
            return this;
        }

        public ScreeningDto build() {
            return new ScreeningDto(movieAttributes, roomAttributes, screeningStartTimer);
        }

    }
}

