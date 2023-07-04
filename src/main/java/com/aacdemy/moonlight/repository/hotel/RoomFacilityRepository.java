package com.aacdemy.moonlight.repository.hotel;

import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RoomFacilityRepository extends JpaRepository<RoomFacility, Long> {
    Optional<RoomFacility> findByFacility(String facility);

    boolean existsByFacility(String facility);

    @Transactional
    @Modifying
    @Query("update RoomFacility r set r.facility = ?1 where r.id = ?2")
    void updateFacility(String facility, Long id);
}