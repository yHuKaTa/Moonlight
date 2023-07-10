package com.aacdemy.moonlight.controller;


import com.aacdemy.moonlight.dto.payment.PaymentRequestDto;

import com.aacdemy.moonlight.service.PaymentService;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

//    private final PayPalHttpClient payPalHttpClient;
//    private final CarTransferRepository carTransferRepository;
//    private final ReservationRepository roomReservationRepository;
//    private final TableReservationRepository tableReservationRepository;
//    private final ScreenReservationRepository screenReservationRepository;
//
//    public PaymentController(PayPalHttpClient payPalHttpClient,
//                             CarTransferRepository carTransferRepository,
//                             ReservationRepository roomReservationRepository,
//                             TableReservationRepository tableReservationRepository,
//                             ScreenReservationRepository screenReservationRepository) {
//        this.payPalHttpClient = payPalHttpClient;
//        this.carTransferRepository = carTransferRepository;
//        this.roomReservationRepository = roomReservationRepository;
//        this.tableReservationRepository = tableReservationRepository;
//        this.screenReservationRepository = screenReservationRepository;
//    }

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/create-payment")
    @Operation(summary = "Payment by paypal",
            description = "Makes payment through paypal using Reservation Type and it's id " +
                    "and returns the order's id and link for approval")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<String> createPayment(@Valid @RequestBody PaymentRequestDto requestDto) {

        Map<String, URL> createdOrderId = paymentService.createPayment(requestDto);

        //String createdOrderId = paymentService.createPayment(requestDto);


//        String authorizedOrderId = paymentService.authorizePayment(createdOrderId
//                .keySet().stream().findFirst().orElse(null));

        return ResponseEntity.ok("OrderId: " + createdOrderId.keySet().stream().findFirst().get()
                + " awaits buyer's approval: " + createdOrderId.values().stream().findFirst().get()
                + " use POST execute after approval");

    }

    @PostMapping("/execute/{approvedOrderId}")
    @PreAuthorize("isAuthenticated")
    public String executePayment(@PathVariable String approvedOrderId) {
        return paymentService.executePayment(approvedOrderId);
    }

}


