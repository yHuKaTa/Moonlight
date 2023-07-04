package com.aacdemy.moonlight.entity.hotel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ROOMS")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
    private Long id;

    @Column(name = "ROOM_NUMBER")
    private int roomNumber;

    @Column(nullable = false, name = "TYPE")
    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Column(nullable = false, name = "VIEW")
    @Enumerated(EnumType.STRING)
    private RoomView view;

    @Column(nullable = false, name = "PRICE")
    @DecimalMin(value = "0.00", message = "Price must be a positive number.")
    private BigDecimal price;

    @Column(nullable = false, name = "PEOPLE", length = 20)
    private int people;

    @JsonBackReference
    @ManyToMany(mappedBy = "room")
    private List<RoomFacility> facilities = new ArrayList<>();
}
