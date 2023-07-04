package com.aacdemy.moonlight.entity.screen;

import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SCREEN_RESERVATIONS")
public class ScreenReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Temporal(TemporalType.DATE) // makes 2023-04-19 21:53:22.854000 to be 2023-04-19 00:00:00.000000
    @Column(name = "DATE", nullable = false)
    private LocalDate date;

    @JoinColumn(name = "EVENT", nullable = false)
    @ManyToOne
    private ScreenEvent event;

    @ManyToMany
    @Column(name = "SEATS", nullable = false)
    private Set<ScreenSeat> seats = new HashSet<>(21);

    @Column(name = "PRICE", nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "PAYMENT_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
