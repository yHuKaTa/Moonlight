package com.aacdemy.moonlight.repository.screen;

import com.aacdemy.moonlight.entity.screen.ScreenEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScreenEventRepository extends JpaRepository<ScreenEvent, Long> {
    @Query("SELECT se FROM ScreenEvent se WHERE se.dateEvent = :date")
   List<ScreenEvent> findAvailableEventsByDate(@Param("date") LocalDate date);
}