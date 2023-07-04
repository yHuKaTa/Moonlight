package com.aacdemy.moonlight.service.impl;
import com.aacdemy.moonlight.dto.screen.ScreenEventRequestDto;
import com.aacdemy.moonlight.entity.screen.Screen;
import com.aacdemy.moonlight.entity.screen.ScreenEvent;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.screen.ScreenEventRepository;
import com.aacdemy.moonlight.repository.screen.ScreenRepository;
import com.aacdemy.moonlight.service.ScreenEventService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScreenEventServiceImpl implements ScreenEventService {
    private final ScreenRepository screenRepository;
    private final ScreenEventRepository screenEventRepository;

    public ScreenEventServiceImpl(ScreenRepository screenRepository, ScreenEventRepository screenEventRepository) {
        this.screenRepository = screenRepository;
        this.screenEventRepository = screenEventRepository;
    }

    @Override
    public ScreenEvent addScreenEvent(ScreenEventRequestDto screenEventRequestDto) {
        Screen screen = screenRepository.findById(screenEventRequestDto.getScreenId()).get();
        if(screenEventRepository.findAvailableEventsByDate(screenEventRequestDto.getDate())
                .size() >= 3){
            throw new EntityNotFoundException("Only three events per dey allowed");
        }        ScreenEvent screenEvent = ScreenEvent.builder()
                .event(screenEventRequestDto.getName())
                .dateEvent(screenEventRequestDto.getDate())
                .screen(screen)
                .build();
        return screenEventRepository.save(screenEvent);
    }

    @Override
    public List<ScreenEvent> findAvailableEventsByDate(LocalDate date) {
        return screenEventRepository.findAvailableEventsByDate(date);
    }
    @Override
    public ScreenEvent findById(Long eventId) {
        return screenEventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("ScreenEvent not found with ID: " + eventId));
    }
}
