package com.aacademy.moonlight.repository;

import com.aacdemy.moonlight.Application;
import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.restaurant.TableReservation;
import com.aacdemy.moonlight.entity.restaurant.TableRestaurant;
import com.aacdemy.moonlight.entity.restaurant.TableZone;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.repository.restaurant.TableReservationRepository;
import com.aacdemy.moonlight.repository.restaurant.TableRestaurantRepository;
import com.aacdemy.moonlight.repository.user.RoleRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
@ContextConfiguration(classes = Application.class)
@ActiveProfiles("test")
public final class TableReservationRepositoryTest {

    @Autowired
    private TableReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableRestaurantRepository tableRestaurantRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        tableRestaurantRepository.deleteAll();
        reservationRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findById_ExistingId_ShouldReturnReservation() {
        // Arrange
        TableReservation reservation = createSampleReservation();
        reservationRepository.save(reservation);

        // Act
        Optional<TableReservation> foundReservation = reservationRepository.findById(reservation.getId());

        // Assert
        assertTrue(foundReservation.isPresent());
        assertEquals(reservation.getId(), foundReservation.get().getId());
    }

    @Test
    void findById_NonExistingId_ShouldReturnEmptyOptional() {
        // Arrange

        // Act
        Optional<TableReservation> foundReservation = reservationRepository.findById(12345L);

        // Assert
        assertFalse(foundReservation.isPresent());
    }

    @Test
    void deleteById_ExistingId_ShouldDeleteReservation() {
        // Arrange
        TableReservation reservation = createSampleReservation();
        reservationRepository.save(reservation);

        // Act
        reservationRepository.deleteById(reservation.getId());

        // Assert
        Optional<TableReservation> deletedReservation = reservationRepository.findById(reservation.getId());
        assertFalse(deletedReservation.isPresent());
    }

    @Test
    void findByUserId_ExistingUserId_ShouldReturnReservations() {
        // Arrange
        User user = createUser();
        TableReservation reservation1 = createSampleReservation();
        reservation1.setUser(user);
        reservationRepository.save(reservation1);

        // Act
        List<TableReservation> reservations = reservationRepository.findByUserId(user.getId());

        // Assert
        assertEquals(1, reservations.size());
        assertTrue(reservations.stream().allMatch(r -> r.getUser().getId().equals(user.getId())));
    }

    @Test
    void findByUserId_NonExistingUserId_ShouldReturnEmptyList() {
        // Arrange

        // Act
        List<TableReservation> reservations = reservationRepository.findByUserId(12345L);

        // Assert
        assertTrue(reservations.isEmpty());
    }

    @Test
    void findByTableId_ExistingTableId_ShouldReturnReservations() {
        // Arrange
        TableRestaurant table = createSampleTable();
        TableReservation reservation1 = createSampleReservation();
        reservation1.setTable(table);
        reservationRepository.save(reservation1);

        // Act
        List<TableReservation> reservations = reservationRepository.findByTableId(table.getId());

        // Assert
        assertEquals(1, reservations.size());
        assertTrue(reservations.stream().allMatch(r -> r.getTable().getId().equals(table.getId())));
    }

    @Test
    void findByTableId_NonExistingTableId_ShouldReturnEmptyList() {
        // Arrange

        // Act
        List<TableReservation> reservations = reservationRepository.findByTableId(12345L);

        // Assert
        assertTrue(reservations.isEmpty());
    }

    @Test
    void findByDate_ExistingDate_ShouldReturnReservations() {
        // Arrange
        TableReservation reservation1 = createSampleReservation();
        reservation1.setDate(LocalDate.now());
        TableReservation reservation2 = createSampleReservation();
        reservation2.setDate(LocalDate.now().plusDays(1));
        TableReservation reservation3 = createSampleReservation();
        reservation3.setDate(LocalDate.now().minusDays(1));
        reservationRepository.saveAll(List.of(reservation1, reservation2, reservation3));

        // Act
        List<TableReservation> reservations = reservationRepository.findByDate(LocalDate.now());

        // Assert
        assertEquals(1, reservations.size());
        assertTrue(reservations.stream().allMatch(r -> r.getDate().equals(LocalDate.now())));
    }

    @Test
    void findByDate_NonExistingDate_ShouldReturnEmptyList() {
        // Arrange

        // Act
        List<TableReservation> reservations = reservationRepository.findByDate(LocalDate.now());

        // Assert
        assertTrue(reservations.isEmpty());
    }

    @Test
    void findByUserAndDate_ExistingUserAndDate_ShouldReturnReservations() {
        // Arrange
        User user = createUser();

        TableReservation reservation1 = createSampleReservation();
        reservation1.setUser(user);
        reservation1.setDate(LocalDate.now());

        TableReservation reservation2 = createSampleReservation();
        reservation2.setUser(user);
        reservation2.setDate(LocalDate.now().plusDays(1));

        TableReservation reservation3 = createSampleReservation();
        reservation3.setUser(user);
        reservation3.setDate(LocalDate.now().minusDays(1));

        reservationRepository.saveAll(List.of(reservation1, reservation2, reservation3));

        // Act
        List<TableReservation> reservations = reservationRepository.findByUserAndDate(user, LocalDate.now());

        // Assert
        assertEquals(1, reservations.size());
        assertTrue(reservations.stream().allMatch(r -> r.getUser().getId().equals(user.getId()) && r.getDate().equals(LocalDate.now())));
    }

    @Test
    void findByUserAndDate_NonExistingUserAndDate_ShouldReturnEmptyList() {
        // Arrange
        User user = createUser();

        // Act
        List<TableReservation> reservations = reservationRepository.findByUserAndDate(user, LocalDate.now());

        // Assert
        assertTrue(reservations.isEmpty());
    }

    private TableReservation createSampleReservation() {
        TableReservation reservation = TableReservation.builder()
                .countPeople(5)
                .date(LocalDate.now())
                .hour(LocalTime.now())
                .paymentStatus(PaymentStatus.PAID)
                .price(10.0)
                .table(createSampleTable())
                .user(createUser())
                .build();
        return reservationRepository.save(reservation);
    }

    private TableRestaurant createSampleTable() {
        TableRestaurant table = TableRestaurant.builder()
                .tableNumber(1)
                .seats(5)
                .zone(TableZone.BAR)
                .isSmoking(true)
                .build();
        return tableRestaurantRepository.save(table);
    }

    private User createUser() {
        String uniqueEmail = "unique_email_" + System.currentTimeMillis() + "@example.com";
        String uniquePassportId = PassportIdGenerator.generatePassportId();

        User user = User.builder()
                .userRole(createUserRole())
                .createdDate(new Date())
                .email(uniqueEmail)
                .firstName("ivan")
                .lastName("ivanov")
                .enabled(true)
                .modifiedDate(null)
                .password("Password123!")
                .phoneNumber("0893943923")
                .passportID(uniquePassportId)
                .build();
        return userRepository.save(user);
    }

    private UserRole createUserRole() {
        UserRole userRole = new UserRole();
        userRole.setUserRole(generateRandomUserRole());
        return roleRepository.save(userRole);
    }

    private String generateRandomUserRole() {
        String prefix = "USER_ROLE";
        int length = 3;
        String randomChars = RandomStringUtils.random(length, true, true).toUpperCase();
        return prefix + randomChars;
    }

    private static class PassportIdGenerator {
        private static final int PASSPORT_ID_LENGTH = 10;

        public static String generatePassportId() {
            StringBuilder sb = new StringBuilder();
            Random random = new Random();

            for (int i = 0; i < PASSPORT_ID_LENGTH; i++) {
                int digit = random.nextInt(10);
                sb.append(digit);
            }
            return sb.toString();
        }
    }
}
