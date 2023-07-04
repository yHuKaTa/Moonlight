package com.aacademy.moonlight.repository;

import com.aacdemy.moonlight.Application;
import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.restaurant.TableReservation;
import com.aacdemy.moonlight.entity.restaurant.TableRestaurant;
import com.aacdemy.moonlight.entity.restaurant.TableZone;
import com.aacdemy.moonlight.repository.restaurant.TableReservationRepository;
import com.aacdemy.moonlight.repository.restaurant.TableRestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class TableRestaurantRepositoryTest {

    @Autowired
    private TableRestaurantRepository tableRestaurantRepository;

    @Autowired
    private TableReservationRepository tableReservationRepository;

    private TableRestaurant table1;
    private TableRestaurant table2;

    @BeforeEach
    void setUp() {
        table1 = createTable(1, TableZone.BAR, true, 4);
        table2 = createTable(2, TableZone.BAR, false, 6);
        tableRestaurantRepository.saveAll(List.of(table1, table2));
    }

    @AfterEach
    void tearDown() {
        tableReservationRepository.deleteAll();
        tableRestaurantRepository.deleteAll();
    }

    @Test
    void findById_ShouldReturnTableById() {
        // Act
        Optional<TableRestaurant> result = tableRestaurantRepository.findById(table1.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(table1.getId(), result.get().getId());
    }

    @Test
    void deleteById_ShouldDeleteTableById() {
        // Act
        tableRestaurantRepository.deleteById(table1.getId());

        // Assert
        Optional<TableRestaurant> result = tableRestaurantRepository.findById(table1.getId());
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void findByZone_ShouldReturnTablesByZone() {
        // Act
        List<TableRestaurant> result = tableRestaurantRepository.findByZone(TableZone.BAR);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(table1));
        Assertions.assertTrue(result.contains(table2));
    }

    @Test
    void findByIsSmoking_ShouldReturnTablesBySmokingStatus() {
        // Act
        List<TableRestaurant> result = tableRestaurantRepository.findByIsSmoking(true);

        // Assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(table1));
    }

    @Test
    void findBySeats_ShouldReturnTablesBySeatCount() {
        // Act
        List<TableRestaurant> result = tableRestaurantRepository.findBySeats(4);

        // Assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(table1));
    }

    @Test
    void findAvailableTableByDateTimeZoneIsSmokingTableIdPeople_ShouldReturnAvailableTables() {
        // Arrange
        LocalDate date = LocalDate.now();
        LocalTime hour = LocalTime.NOON;
        boolean isSmoking = false;
        int seats = 4;

        TableReservation reservation = createReservation(date, hour, table2, seats);
        tableReservationRepository.save(reservation);

        // Act
        List<TableRestaurant> availableTables = tableRestaurantRepository.findAvailableTableByDateTimeZoneIsSmokingTableIdPeople(
                date, Optional.of(hour), Optional.of(TableZone.BAR), Optional.of(isSmoking), Optional.empty(), Optional.of(seats));

        // Assert
        Assertions.assertEquals(0, availableTables.size());
    }

    // Helper methods

    private TableRestaurant createTable(int tableNumber, TableZone zone, boolean isSmoking, int seats) {
        return TableRestaurant.builder()
                .tableNumber(tableNumber)
                .zone(zone)
                .isSmoking(isSmoking)
                .seats(seats)
                .build();
    }

    private TableReservation createReservation(LocalDate date, LocalTime hour, TableRestaurant table, int seats) {
        return TableReservation.builder()
                .date(date)
                .hour(hour)
                .table(table)
                .user(null)
                .paymentStatus(PaymentStatus.PAID)
                .countPeople(seats)
                .price(20.0)
                .build();
    }
}

