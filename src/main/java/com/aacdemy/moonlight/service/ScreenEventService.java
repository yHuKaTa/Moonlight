package com.aacdemy.moonlight.service;
import com.aacdemy.moonlight.dto.screen.ScreenEventRequestDto;
import com.aacdemy.moonlight.entity.screen.ScreenEvent;

import java.time.LocalDate;
import java.util.List;

public interface ScreenEventService {

    ScreenEvent addScreenEvent(ScreenEventRequestDto screenEventRequestDto);

    List<ScreenEvent> findAvailableEventsByDate(LocalDate date);
    ScreenEvent findById(Long eventId);
}
