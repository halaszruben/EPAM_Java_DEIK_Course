package com.epam.training.ticketservice.core.screening.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Screening {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer movieId;

    private Integer roomId;

    private LocalDateTime screeningStartTimer;

    public Screening(Integer movieId, Integer roomId, LocalDateTime screeningStartTimer) {
        this.movieId = movieId;
        this.roomId = roomId;
        this.screeningStartTimer = screeningStartTimer;
    }

}
