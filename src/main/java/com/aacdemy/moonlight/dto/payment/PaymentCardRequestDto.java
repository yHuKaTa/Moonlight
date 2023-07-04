package com.aacdemy.moonlight.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PaymentCardRequestDto extends PaymentRequestDto{

    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotBlank(message = "Expiration date is required in format MM/yy")
    @Size(min = 5, max = 5)
    private String expirationDate;

    @NotBlank(message = "CVV code is required")
    @Size(min = 3, max = 3)
    private String cvvCode;

    public PaymentCardRequestDto(String reservationType, Long id, String cardNumber, String expirationDate, String cvvCode) {
        super(reservationType, id);
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvvCode = cvvCode;
    }

}
