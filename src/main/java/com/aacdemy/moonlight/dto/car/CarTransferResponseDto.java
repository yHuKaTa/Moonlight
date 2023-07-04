package com.aacdemy.moonlight.dto.car;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CarTransferResponseDto {

    @Builder.Default
    private final String message = "Your reservation for car transfer from/to Moonlight Hotel & Spa successful was successful. " + "Car transfer details:";

    private String id;

    private String make;

    private String model;

    private String seats;

    private LocalDate date;

    private String price;
}
