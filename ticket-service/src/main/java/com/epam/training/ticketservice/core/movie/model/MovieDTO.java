package com.epam.training.ticketservice.core.movie.model;

import lombok.Value;

@Value
public class MovieDTO {

    private final String movieTitle;
    private final String movieType;
    private final int movieLength;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String movieTitle;
        private String movieType;
        private int movieLength;

        public Builder withMovieTitle(String movieTitle) {
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder withMovieType(String movieType) {
            this.movieType = movieType;
            return this;
        }

        public Builder withMovieLength(int movieLength) {
            this.movieLength = movieLength;
            return this;
        }

        public MovieDTO build(){
            return new MovieDTO(movieTitle, movieType, movieLength);
        }

    }
}
