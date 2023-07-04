package com.aacdemy.moonlight.service;
import com.aacdemy.moonlight.dto.screen.ScreenEventRequestDto;
import com.aacdemy.moonlight.entity.screen.Screen;
import com.aacdemy.moonlight.entity.screen.ScreenEvent;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.screen.ScreenEventRepository;
import com.aacdemy.moonlight.repository.screen.ScreenRepository;
import com.aacdemy.moonlight.service.impl.ScreenEventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class ScreenEventServiceImplTest {
    @Mock
    private ScreenRepository screenRepository;
    @Mock
    private ScreenEventRepository screenEventRepository;
    @InjectMocks
    private ScreenEventServiceImpl screenEventService;

    @Test
    void addEvent_ShouldThrowException_WhenInvalid() {
        ScreenEventRequestDto requestDto = new ScreenEventRequestDto();
        requestDto.setScreenId(1L);
        requestDto.setName("First event");
        requestDto.setDate(LocalDate.ofEpochDay(2023 - 05 - 31));

        Screen screen = new Screen();
        screen.setId(1L);
        screen.setName("Screen 1");
        when(screenRepository.findById(requestDto.getScreenId())).thenReturn(Optional.of(screen));

        List<ScreenEvent> events = new ArrayList<>();
        events.add(new ScreenEvent());
        events.add(new ScreenEvent());
        events.add(new ScreenEvent());
        when(screenEventRepository.findAvailableEventsByDate(requestDto.getDate())).thenReturn(events);
        assertThrows(EntityNotFoundException.class, () -> screenEventService.addScreenEvent(requestDto));

        when(screenRepository.findById(1L)).thenReturn(Optional.empty());
        verify(screenRepository, times(1)).findById(requestDto.getScreenId());
        verify(screenEventRepository, times(1)).findAvailableEventsByDate(requestDto.getDate());
        verify(screenEventRepository, never()).save(any(ScreenEvent.class));

    }
    @Test
    void addEvent_ShouldAddEvent_WhenValid() {
        ScreenEventRequestDto requestDto = new ScreenEventRequestDto();
        requestDto.setScreenId(1L);
        requestDto.setName("First event");
        requestDto.setDate(LocalDate.of(2023, 5, 31));

        Screen screen = new Screen();
        screen.setId(1L);
        screen.setName("Screen 1");
        when(screenRepository.findById(requestDto.getScreenId())).thenReturn(Optional.of(screen));

        List<ScreenEvent> events = new ArrayList<>();
        when(screenEventRepository.findAvailableEventsByDate(requestDto.getDate())).thenReturn(events);

        ScreenEvent savedEvent = new ScreenEvent();
        when(screenEventRepository.save(any(ScreenEvent.class))).thenReturn(savedEvent);

        ScreenEvent result = screenEventService.addScreenEvent(requestDto);

        verify(screenRepository, times(1)).findById(requestDto.getScreenId());
        verify(screenEventRepository, times(1)).findAvailableEventsByDate(requestDto.getDate());
        verify(screenEventRepository, times(1)).save(any(ScreenEvent.class));

        assertEquals(savedEvent, result);
    }
    @Test
    void test_find_AvailableEventsByDate() {
        LocalDate date = LocalDate.of(2023, 5, 31);

        List<ScreenEvent> events = new ArrayList<>();
        events.add(new ScreenEvent());
        events.add(new ScreenEvent());
        events.add(new ScreenEvent());
        when(screenEventRepository.findAvailableEventsByDate(date)).thenReturn(events);

        List<ScreenEvent> result = screenEventService.findAvailableEventsByDate(date);

        assertEquals(events, result);
        verify(screenEventRepository, times(1)).findAvailableEventsByDate(date);
    }
}
