package com.aacdemy.moonlight.entity.restaurant;

import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TABLE_RESERVATIONS")
public class TableReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "DATE", nullable = false)
    @Temporal(TemporalType.DATE) // makes 2023-04-19 21:53:22.854000 to be 2023-04-19 00:00:00.000000
    private LocalDate date;

    @Column(name = "RESERVATION_HOUR", nullable = false)
    private LocalTime hour;

    @Column(name = "PRICE")
    private double price;

    @ManyToOne
    @JoinColumn(name = "TABLE_NUMBER")
    private TableRestaurant table;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "PAYMENT_STATUS")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "COUNT_PEOPLE")
    private int countPeople;

}