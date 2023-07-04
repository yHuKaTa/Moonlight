package com.aacdemy.moonlight.repository;

import com.aacdemy.moonlight.entity.screen.Screen;
import com.aacdemy.moonlight.entity.screen.ScreenEvent;
import com.aacdemy.moonlight.repository.screen.ScreenEventRepository;
import com.aacdemy.moonlight.repository.screen.ScreenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScreenEventRepositoryTest {
    @Autowired
    private ScreenEventRepository screenEventRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @BeforeAll
    public void init() {
        Screen screen = screenRepository.save(Screen.builder().name("ScreenOne").build());
        ScreenEvent event = screenEventRepository.save(ScreenEvent.builder().dateEvent(LocalDate.now()).event("EventFast").screen(screen).build());
    }

    @Test
    public void save() {
        init();
        Optional<ScreenEvent> result = screenEventRepository.findById(2L);
        Assertions.assertEquals("EventFast", result.get().getEvent());
        Assertions.assertEquals("ScreenOne", result.get().getScreen().getName());
    }

    @Test
    public void findAvailableEventsByDateReturnList() {
        Assertions.assertFalse(screenEventRepository.findAvailableEventsByDate(LocalDate.now()).isEmpty());
    }

    @Test
    public void findBySeatPositionAndScreenReturnNull() {
        Assertions.assertTrue(screenEventRepository.findAvailableEventsByDate(LocalDate.of(2018, 12, 31)).isEmpty());
    }
}
