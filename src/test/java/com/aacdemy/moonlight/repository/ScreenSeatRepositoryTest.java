package com.aacdemy.moonlight.repository;

import com.aacdemy.moonlight.entity.screen.Screen;
import com.aacdemy.moonlight.entity.screen.ScreenSeat;
import com.aacdemy.moonlight.repository.screen.ScreenRepository;
import com.aacdemy.moonlight.repository.screen.ScreenSeatsRepository;
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

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScreenSeatRepositoryTest {
    @Autowired
    private ScreenSeatsRepository screenSeatsRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @BeforeAll
    public void init() {
        Screen screen = screenRepository.save(Screen.builder().name("ScreenSevenUp").build());
        ScreenSeat seat = screenSeatsRepository.save(ScreenSeat.builder().seatPosition(1).screen(screen).build());
        ScreenSeat seatTwo = screenSeatsRepository.save(ScreenSeat.builder().seatPosition(2).screen(screen).build());
        Screen emptyScreen = screenRepository.save(Screen.builder().name("Empty").build());
    }

    @Test
    public void saveAll() {
        Assertions.assertFalse(screenSeatsRepository.findAll().isEmpty());
    }

    @Test
    public void findBySeatPositionAndScreenReturnSeat() {
        Screen screen = screenRepository.findByName("ScreenSevenUp").get();
        Assertions.assertNotNull(screenSeatsRepository.findBySeatPositionAndScreen(1, screen));
        Assertions.assertEquals(screenSeatsRepository.findById(1L).get().getSeatPosition(), screenSeatsRepository.findBySeatPositionAndScreen(1, screen).get().getSeatPosition());
        Assertions.assertEquals("ScreenSevenUp", screenSeatsRepository.findBySeatPositionAndScreen(1, screen).get().getScreen().getName());
    }

    @Test
    public void findBySeatPositionAndScreenReturnNull() {
        Screen screen = screenRepository.findByName("Empty").get();
        Assertions.assertTrue(screenSeatsRepository.findBySeatPositionAndScreen(3, screen).isEmpty());
    }

    @Test
    public void findByScreenReturnList() {
        Screen screen = screenRepository.findByName("ScreenSevenUp").get();
        Assertions.assertNotNull(screenSeatsRepository.findByScreen(screen));
        Assertions.assertFalse(screenSeatsRepository.findByScreen(screen).isEmpty());
    }

    @Test
    public void findByScreenReturnEmptyList() {
        Screen screen = screenRepository.findByName("Empty").get();
        Assertions.assertNotNull(screenSeatsRepository.findByScreen(screen));
        Assertions.assertTrue(screenSeatsRepository.findByScreen(screen).isEmpty());
    }
}
