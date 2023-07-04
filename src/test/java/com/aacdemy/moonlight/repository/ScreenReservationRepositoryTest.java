package com.aacdemy.moonlight.repository;

import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.screen.Screen;
import com.aacdemy.moonlight.entity.screen.ScreenEvent;
import com.aacdemy.moonlight.entity.screen.ScreenReservation;
import com.aacdemy.moonlight.entity.screen.ScreenSeat;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.repository.screen.ScreenEventRepository;
import com.aacdemy.moonlight.repository.screen.ScreenRepository;
import com.aacdemy.moonlight.repository.screen.ScreenReservationRepository;
import com.aacdemy.moonlight.repository.screen.ScreenSeatsRepository;
import com.aacdemy.moonlight.repository.user.RoleRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
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

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScreenReservationRepositoryTest {
    @Autowired
    private ScreenReservationRepository screenReservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScreenEventRepository screenEventRepository;

    @Autowired
    private ScreenSeatsRepository screenSeatsRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    public void init() {
        UserRole roleAdmin = new UserRole();
        roleAdmin.setUserRole("admin");
        roleAdmin = roleRepository.save(roleAdmin);
        User userAdmin = User.builder().id(1L).firstName("Dimitar").lastName("Enev").email("admin@email.bg").phoneNumber("+3598888888").passportID("224393459").password("hegui123984gqhi!GJF").userRole(roleAdmin).createdDate(Date.from(Instant.now())).enabled(true).build();
        userAdmin = userRepository.save(userAdmin);
        UserRole role = new UserRole();
        role.setUserRole("user");
        role = roleRepository.save(role);
        User user = User.builder().id(2L).firstName("Dimitar").lastName("Enev").email("user@email.bg").phoneNumber("+359888888888").passportID("2243934589").password("hegui123984gqhi!GJF").userRole(role).createdDate(Date.from(Instant.now())).enabled(true).build();
        user = userRepository.save(user);
        Screen screen = Screen.builder().name("ScreenFive").build();
        screen = screenRepository.save(screen);
        Screen screenTwo = Screen.builder().name("ScreenSix").build();
        screenTwo = screenRepository.save(screenTwo);
        ScreenEvent event = ScreenEvent.builder().event("Event").dateEvent(LocalDate.of(2023, 6, 8)).screen(screen).build();
        event = screenEventRepository.save(event);
        ScreenSeat seatOne = screenSeatsRepository.save(ScreenSeat.builder().screen(screen).seatPosition(1).build());
        ScreenSeat seatTwo = screenSeatsRepository.save(ScreenSeat.builder().screen(screen).seatPosition(2).build());
        Set<ScreenSeat> seats = Set.of(seatOne, seatTwo);
        ScreenReservation reservation = ScreenReservation.builder().date(LocalDate.of(2023,6,5)).user(user).event(event).price(20.00).status(PaymentStatus.UNPAID).seats(seats).build();
        screenReservationRepository.save(reservation);
    }

    @Test
    public void findByUserIdReturnList() {
        Long id = userRepository.findByEmail("user@email.bg").get().getId();
        Assertions.assertFalse(screenReservationRepository.findByUserId(id).isEmpty());
        Assertions.assertTrue(screenReservationRepository.findByUserId(id).contains(screenReservationRepository.findById(1L).get()));
    }

    @Test
    public void findByUserIdReturnEmpty() {
        Assertions.assertTrue(screenReservationRepository.findByUserId(1L).isEmpty());
    }

    @Test
    public void findScreenReservationsByDateReturnList() {
        Assertions.assertFalse(screenReservationRepository.findScreenReservationsByDate(LocalDate.of(2023, 6, 5)).isEmpty());
        Assertions.assertTrue(screenReservationRepository.findScreenReservationsByDate(LocalDate.of(2023, 6, 5)).contains(screenReservationRepository.findById(1L).get()));
    }

    @Test
    public void findScreenReservationsByDateReturnEmptyList() {
        Assertions.assertTrue(screenReservationRepository.findScreenReservationsByDate(LocalDate.of(2023, 6, 12)).isEmpty());
    }

    @Test
    public void findByDateAndSeatsScreenReturnList() {
        Screen screen = screenRepository.findByName("ScreenFive").get();
        Assertions.assertFalse(screenReservationRepository.findByDateAndSeatsScreen(LocalDate.of(2023, 6, 8), screen).isEmpty());
    }

    @Test
    public void findByDateAndSeatsScreenReturnEmptyList() {
        Screen screen = Screen.builder().name("ScreenSix").build();
        screenRepository.save(screen);
        Assertions.assertTrue(screenReservationRepository.findByDateAndSeatsScreen(LocalDate.of(2023, 6, 8), screen).isEmpty());
    }

    @Test
    public void save() {
        Assertions.assertNotNull(screenReservationRepository.findById(1L));
        Assertions.assertEquals("Event", screenReservationRepository.findById(1L).get().getEvent().getEvent());
    }
}
