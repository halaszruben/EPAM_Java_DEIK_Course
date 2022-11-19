package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    String createScreening(String movieName, String roomName, LocalDateTime screeningStartTimer);

    void deleteScreening(ScreeningDto screeningDto);

    List<ScreeningDto> listScreenings();

}
