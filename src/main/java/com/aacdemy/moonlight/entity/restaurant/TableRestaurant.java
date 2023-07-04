package com.aacdemy.moonlight.entity.restaurant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "RESTAURANT_TABLES")
public class TableRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "TABLE_NUMBER")
    private int tableNumber;

    @Column(name = "ZONE", nullable = false)
    @Enumerated(EnumType.STRING)
    private TableZone zone;

    @Column(name = "IS_SMOKING", nullable = false)
    private boolean isSmoking;

    @Column(name = "SEATS", nullable = false)
    private int seats;

}
