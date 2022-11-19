package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    Optional<Screening> findScreeningByMovieIdAndRoomIdAndScreeningStartTimer(
            Integer movieId, Integer roomId, LocalDateTime screeningStartTimer);

    Optional<Screening> findScreeningByMovieIdEqualsAndScreeningStartTimer(
            Integer movieId, LocalDateTime screeningStartTimer);

    Optional<Screening> findScreeningByMovieIdEqualsAndScreeningStartTimerIsGreaterThanEqual(
            Integer movieId, LocalDateTime screeningStartTimer);
}
