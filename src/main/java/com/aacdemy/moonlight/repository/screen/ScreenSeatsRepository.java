package com.aacdemy.moonlight.repository.screen;

import com.aacdemy.moonlight.entity.screen.Screen;
import com.aacdemy.moonlight.entity.screen.ScreenSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenSeatsRepository extends JpaRepository<ScreenSeat, Long> {
    @Query("select s from ScreenSeat s where s.seatPosition = ?1 and s.screen = ?2")
    Optional<ScreenSeat> findBySeatPositionAndScreen(int seatPosition, Screen screen);
    List<ScreenSeat> findByScreen(Screen screen);
}
