package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    String createScreening(String movieName, String roomName, LocalDateTime screeningStartTimer);

    void deleteScreening(String movieName, String roomName, LocalDateTime time);

    List<ScreeningDto> listScreenings();

}
