package com.aacdemy.moonlight.repository.hotel;

import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomBedType;
import com.aacdemy.moonlight.entity.hotel.RoomReservation;
import com.aacdemy.moonlight.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<RoomReservation, Long> {
    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.paymentStatus = ?1 where r.id = ?2")
    void updateStatusById(PaymentStatus paymentStatus, Long id);

    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.room = ?1 where r.id = ?2")
    void updateRoomType(Room room, Long id);

    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.endDate = ?1 where r.id = ?2")
    void updateEndDate(LocalDate endDate, Long id);

    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.startDate = ?1 where r.id = ?2")
    void updateStartDate(LocalDate startDate, Long id);

    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.room = ?1, r.adults = ?2 where r.id = ?3")
    void updateRoomAndAdults(Room room, int adults, Long id);

    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.adults = ?1 where r.id = ?2")
    void updateAdults(int adults, Long id);

    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.children = ?1 where r.id = ?2")
    void updateChildren(int children, Long id);

    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.bedType = ?1 where r.id = ?2")
    void updateBedType(RoomBedType bedType, Long id);

    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.room = ?1 where r.id = ?2")
    void updateRoomView(Room room, Long id);

    @Transactional
    @Modifying
    @Query("update RoomReservation r set r.paymentStatus = ?1 where r.id = ?2")
    void updatePaymentStatus(PaymentStatus paymentStatus, Long id);

    List<RoomReservation> findByUser(User user);

    List<RoomReservation> findByRoomRoomNumber(Long id);

    @Query("SELECT r FROM RoomReservation r" +
            " WHERE (r.startDate BETWEEN :startDate AND :endDate)" +
            " AND (r.endDate BETWEEN :startDate AND :endDate)")
    List<RoomReservation> getRoomReservationsByStartDateAndEndDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM RoomReservation r" +
            " WHERE (r.startDate BETWEEN :startDate AND :endDate)" +
            " AND (r.endDate BETWEEN :startDate AND :endDate)" +
            " AND (r.user = :user )")
    List<RoomReservation> getRoomReservationsByStartDateAndEndDateAndUser(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("user") User user);

    boolean existsByRoomAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Room room, LocalDate startDate, LocalDate endDate);

    @Query("select (count(r) <= 0) from RoomReservation r where r.startDate >= ?1 and r.endDate <= ?2 and r.room.id = ?3 and r.id != ?4")
    boolean isRoomNotOccupied(LocalDate startDate, LocalDate endDate, Long roomId, Long id);

    @Modifying
    @Query(value = "DELETE FROM RoomReservation WHERE id = :test")
    void deleteRoomReservationById(@Param("test") Long id);

    @Query("select r.room from RoomReservation r where (r.startDate >= ?1 and r.startDate < ?2) or (r.endDate > ?1 and r.endDate <= ?2)")
    List<Room> getRoomByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);


}