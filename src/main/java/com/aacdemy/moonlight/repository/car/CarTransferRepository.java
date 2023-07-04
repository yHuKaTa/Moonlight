package com.aacdemy.moonlight.repository.car;

import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.car.Car;
import com.aacdemy.moonlight.entity.car.CarTransfer;
import com.aacdemy.moonlight.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarTransferRepository extends JpaRepository<CarTransfer, Long> {
    @Transactional
    @Modifying
    @Query("update CarTransfer c set c.status = ?1 where c.id = ?2")
    void updateStatusById(PaymentStatus status, Long id);

    @Query("SELECT c FROM Car c WHERE c.id NOT IN "
            + "(SELECT ct.car.id FROM CarTransfer ct WHERE ct.date = :date)")
    List<Car> findAvailableCarsByDate(@Param("date") Date date);

    /**
     Returns a list of all available cars for a given date, car category, model and number of seats.
     A car is considered available if it is not scheduled for a transfer on the given date.
     If any of the parameters is null, it is ignored.
     @param date The date for which to check car availability.
     @param seats The number of seats to filter by.
     @param carCategoryID The car category to filter by.
     @param model The car model to filter by.
     @return A list of all available cars that match the specified filters.
     */
    @Query("SELECT c FROM Car c "
            + "JOIN c.carCategory cc "
            + "WHERE c.id NOT IN (SELECT ct.car.id FROM CarTransfer ct WHERE ct.date = :date)"
            + "AND (:carCategoryID IS NULL OR cc.id = :carCategoryID) "
            + "AND (:make IS NULL OR c.make = :make) "
            + "AND (:model IS NULL OR c.model = :model) "
            + "AND (:seats IS NULL OR cc.seats >= :seats)")

    List<Car> findAvailableCarsByDateSeatsCarCategoryIdMakeModel(
            @Param("date") Date date,
            @Param("seats") Optional<Integer> seats,
            @Param("carCategoryID") Optional<Long> carCategoryID,
            @Param("make") Optional<String> make,
            @Param("model") Optional<String> model);

    List<CarTransfer> findByUser(User user);

    List<CarTransfer> findByCar(Car car);

}
