package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.payment.PaymentCardRequestDto;
import com.aacdemy.moonlight.dto.payment.PaymentRequestDto;

public interface PaymentService {

    String createPayment(PaymentRequestDto requestDto);
   // String createCardPayment(PaymentCardRequestDto requestDto);
}
