package com.aacdemy.moonlight.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentRequestDto {

    /**
     * Type of reservation (possible values: CarTransfer, RoomReservation, TableReservation, ScreenReservation)
     */
//    @Pattern(regexp = "^(CarTransfer|RoomReservation|TableReservation|ScreenReservation)$",
//            message = "Invalid reservation type. Possible values: CarTransfer, RoomReservation, TableReservation, ScreenReservation")
//    with the Pattern above we get:
//    {"errors": "HV000030: No validator could be found for constraint 'jakarta.validation.constraints.Pattern'
//    validating type 'java.lang.Object'. Check configuration for 'reservationType'"
//      }
//     the Reservation type is regulated in the Payment Controller
    @NotNull(message = "ReservationType is required. Possible values: CarTransfer, RoomReservation, TableReservation, ScreenReservation")
    Object reservationType;

    @NotNull(message = "Id of the Reservation is required")
    Long id;
}
