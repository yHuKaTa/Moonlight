package com.aacdemy.moonlight.service;

import com.aacdemy.moonlight.dto.payment.PaymentCardRequestDto;
import com.aacdemy.moonlight.dto.payment.PaymentRequestDto;

import java.net.URL;
import java.util.Map;

public interface PaymentService {

    Map<String, URL> createPayment(PaymentRequestDto requestDto);

    String executePayment(String orderId);
    String authorizePayment(String orderId);
    // String createCardPayment(PaymentCardRequestDto requestDto);
}
