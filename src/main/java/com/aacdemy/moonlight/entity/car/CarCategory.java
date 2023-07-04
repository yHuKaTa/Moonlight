package com.aacdemy.moonlight.entity.car;

import com.aacdemy.moonlight.entity.car.enums.CarType;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CAR_CATEGORIES")
public class CarCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(nullable = false, name = "CAR_TYPE", unique = true)
    @Enumerated(EnumType.STRING)
    private CarType type;

    @Column(nullable = false, name = "SEATS")
    private int seats;

    @Column(nullable = false, name = "PRICE_PER_DAY")
    private double pricePerDay;
}
