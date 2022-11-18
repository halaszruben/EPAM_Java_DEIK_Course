package com.epam.training.ticketservice.core.movie.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String movieTitle;

    private String movieType;

    private Integer movieLength;

    public Movie(String movieTitle, String movieType, Integer movieLength) {
        this.movieTitle = movieTitle;
        this.movieType = movieType;
        this.movieLength = movieLength;
    }

}
