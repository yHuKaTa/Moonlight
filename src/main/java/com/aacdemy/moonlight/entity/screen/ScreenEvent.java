package com.aacdemy.moonlight.entity.screen;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SCREEN_EVENTS")
public class ScreenEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "EVENTS", nullable = false)
    private String event;

    @Column(name = "DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dateEvent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCREEN_ID")
    private Screen screen;
}


