package com.aacdemy.moonlight.repository.restaurant;

import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.restaurant.TableReservation;
import com.aacdemy.moonlight.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface TableReservationRepository extends JpaRepository<TableReservation, Long> {
    @Transactional
    @Modifying
    @Query("update TableReservation t set t.paymentStatus = ?1 where t.id = ?2")
    void updateStatusById(PaymentStatus paymentStatus, Long id);

    Optional<TableReservation> findById(Long id);

    void deleteById(Long id);

    List<TableReservation> findByUserId(Long userId);

    List<TableReservation> findByTableId(Long tableId);

    List<TableReservation> findByDate(LocalDate date);

    List<TableReservation> findByUserAndDate(User user, LocalDate date);

}
