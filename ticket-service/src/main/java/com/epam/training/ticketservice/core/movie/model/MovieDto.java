package com.epam.training.ticketservice.core.movie.model;

import lombok.Value;

@Value
public class MovieDto {

    private final String movieTitle;
    private final String movieType;
    private final Integer movieLength;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String movieTitle;
        private String movieType;
        private Integer movieLength;

        public Builder withMovieTitle(String movieTitle) {
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder withMovieType(String movieType) {
            this.movieType = movieType;
            return this;
        }

        public Builder withMovieLength(Integer movieLength) {
            this.movieLength = movieLength;
            return this;
        }

        public MovieDto build(){
            return new MovieDto(movieTitle, movieType, movieLength);
        }

    }
}
