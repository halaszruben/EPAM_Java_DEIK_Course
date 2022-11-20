package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    void deleteScreeningByMovieIdAndRoomIdAndScreeningStartTimer(Integer movieId, Integer roomId, LocalDateTime time);

}
