package com.aacdemy.moonlight.entity.screen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SCREEN_SEATS")
public class ScreenSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "POSITION", nullable = false)
    private int seatPosition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCREEN_ID")
    private Screen screen;

}
