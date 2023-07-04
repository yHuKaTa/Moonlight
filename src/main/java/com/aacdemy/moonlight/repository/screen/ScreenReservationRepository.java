package com.aacdemy.moonlight.repository.screen;

import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.screen.Screen;
import com.aacdemy.moonlight.entity.screen.ScreenReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ScreenReservationRepository extends JpaRepository<ScreenReservation, Long> {
//    @Transactional
//    @Modifying
//    @Query("update ScreenReservation s set s.status = ?1 where s.id = ?2")
//    void updateStatusById(PaymentStatus status, Long id);

    @Query("select sr from ScreenReservation sr where sr.event.dateEvent = ?1 and sr.event.screen = ?2")
    List<ScreenReservation> findByDateAndSeatsScreen(LocalDate date, Screen screen);
    List<ScreenReservation> findAll();
    List<ScreenReservation> findByUserId(Long userId);
    @Query("SELECT sr FROM ScreenReservation sr WHERE sr.date = :date")
    List<ScreenReservation> findScreenReservationsByDate(@Param("date") LocalDate date);
}
