package com.aacdemy.moonlight.dto.restaurant;

import com.aacdemy.moonlight.entity.restaurant.TableRestaurant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TableReservationResponseDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hour;
    private TableRestaurant tableRestaurant;
    private int tableNumber;
    private int people;
}
