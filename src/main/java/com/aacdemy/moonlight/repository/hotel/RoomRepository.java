package com.aacdemy.moonlight.repository.hotel;

import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(int roomNumber);

    List<Room> findByType(RoomType roomType);

    List<Room> findByView(RoomView roomView);

    List<Room> findByPrice(BigDecimal price);

    List<Room> findByPeople(int people);

    @Query("select r from Room r inner join r.facilities facilities where facilities.facility = ?1")
    List<Room> findByFacilitiesContaining(String facility);

}