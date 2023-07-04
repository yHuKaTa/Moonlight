package com.aacdemy.moonlight.repository;

import com.aacdemy.moonlight.entity.screen.Screen;
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

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScreenRepositoryTest {
    @Autowired
    private ScreenRepository screenRepository;

    @BeforeAll
    public void init() {
        screenRepository.save(Screen.builder().id(1L).name("ScreenOme").build());
        screenRepository.saveAll(List.of(Screen.builder().name("Izmama").build(), Screen.builder().name("ScreenTwo").build(), Screen.builder().name("ScreenThree").build()));
    }

    @Test
    public void saveScreen() {
        Screen screen = screenRepository.save(Screen.builder().name("ScreenFour").build());
        Assertions.assertNotNull(screenRepository.findByName("ScreenFour"));
        Assertions.assertEquals(screen.getName(), screenRepository.findByName("ScreenFour").get().getName());
    }

    @Test
    public void saveAllScreen() {
        Assertions.assertNotNull(screenRepository.findByName("Izmama"));
        Assertions.assertNotNull(screenRepository.findByName("ScreenTwo"));
        Assertions.assertNotNull(screenRepository.findByName("ScreenThree"));
        Assertions.assertEquals("Izmama", screenRepository.findByName("Izmama").get().getName());
        Assertions.assertEquals("ScreenTwo", screenRepository.findByName("ScreenTwo").get().getName());
        Assertions.assertEquals("ScreenThree", screenRepository.findByName("ScreenThree").get().getName());
    }

    @Test
    public void findAllReturnList() {
        List<Screen> screens = screenRepository.findAll();
        Assertions.assertFalse(screens.isEmpty());
    }

    @Test
    public void findByNameTestReturnScreen() {
        Assertions.assertEquals("ScreenTwo", screenRepository.findByName("ScreenTwo").get().getName());
    }

    @Test
    public void findByNameTestReturnEmpty() {
        Assertions.assertEquals(Optional.empty(), screenRepository.findByName("ScreenSeven"));
    }

    @Test
    public void findByIdReturnScreen() {
        Optional<Screen> result = screenRepository.findById(1L);
        Assertions.assertEquals("ScreenOme", result.get().getName());
    }

    @Test
    public void findByIdTestReturnEmpty() {
        Assertions.assertEquals(Optional.empty(), screenRepository.findById(85L));
    }
}
