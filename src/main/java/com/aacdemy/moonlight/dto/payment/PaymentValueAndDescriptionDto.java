package com.aacdemy.moonlight.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
public class PaymentValueAndDescriptionDto {
    BigDecimal paymentValue;
    String paymentDescription;
}
