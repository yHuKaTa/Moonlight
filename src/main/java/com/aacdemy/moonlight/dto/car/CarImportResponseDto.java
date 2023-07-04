package com.aacdemy.moonlight.dto.car;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CarImportResponseDto {
    private String make;
    private String model;
    private String year;
    private String type;
    private String seats;
    private String pricePerDay;
}
