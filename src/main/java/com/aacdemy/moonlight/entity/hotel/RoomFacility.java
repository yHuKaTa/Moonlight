package com.aacdemy.moonlight.entity.hotel;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ROOM_FACILITIES")
public class RoomFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FACILITY_ID")
    private Long id;

    @Column(name = "FACILITY")
    private String facility;

    @ManyToMany
    @JsonManagedReference
    private List<Room> room;
}