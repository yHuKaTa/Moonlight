package com.aacdemy.moonlight.repository.restaurant;

import com.aacdemy.moonlight.entity.restaurant.TableRestaurant;
import com.aacdemy.moonlight.entity.restaurant.TableZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TableRestaurantRepository extends JpaRepository<TableRestaurant, Long> {

    Optional<TableRestaurant> findById(Long id);

    void deleteById(Long id);

    List<TableRestaurant> findByZone(TableZone zone);

    List<TableRestaurant> findByIsSmoking(boolean isSmoking);

    List<TableRestaurant> findBySeats(int seats);

    @Query("SELECT t FROM TableRestaurant t " +
            "WHERE t.id NOT IN (" +
            "   SELECT tr.table.id FROM TableReservation tr " +
            "   WHERE tr.date = :date " +
            "   AND (:hour is NOT NULL OR :hour is NULL or tr.hour = :hour) " +
            ") " +
            "AND (:tableZone IS NULL OR t.zone = :tableZone) " +
            "AND (:isSmoking IS NULL OR t.isSmoking = :isSmoking) " +
            "AND (:tableId IS NULL OR t.id = :tableId) " +
            "AND (:seats IS NULL OR t.seats >= :seats)")
    List<TableRestaurant> findAvailableTableByDateTimeZoneIsSmokingTableIdPeople(
            @Param("date") LocalDate date,
            @Param("hour") Optional<LocalTime> hour,
            @Param("tableZone") Optional<TableZone> tableZone,
            @Param("isSmoking") Optional<Boolean> isSmoking,
            @Param("tableId") Optional<Long> tableId,
            @Param("seats") Optional<Integer> seats);
}
