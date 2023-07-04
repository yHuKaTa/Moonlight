package com.aacdemy.moonlight.controller;


import com.aacdemy.moonlight.dto.payment.PaymentRequestDto;

import com.aacdemy.moonlight.service.PaymentService;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
            description = "Makes payment through paypal using Reservation Type and it's id and returns the payment's id")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<String> createPayment(@Valid @RequestBody PaymentRequestDto requestDto) {
        return ResponseEntity.ok(paymentService.createPayment(requestDto));
    }
//        Object reservation;
//
//        // Разпознаване на обекта
//        Long id = requestDto.getId();
//
//        if (requestDto.getReservationType().equals("CarTransfer")) {
//            reservation = carTransferRepository.findById(id).orElse(null);
//        } else if (requestDto.getReservationType().equals("RoomReservation")) {
//            reservation = roomReservationRepository.findById(id).orElse(null);
//        } else if (requestDto.getReservationType().equals("TableReservation")) {
//            reservation = tableReservationRepository.findById(id).orElse(null);
//        } else if (requestDto.getReservationType().equals("EventReservation")) {
//            reservation = screenReservationRepository.findById(id).orElse(null);
//        } else {
//            return "Invalid reservation type. Possible values: CarTransfer, RoomReservation, TableReservation, ScreenReservation";
//        }
//
//        if (reservation == null) {
//            return "Reservation not found";
//        }
//
//        BigDecimal paymentValue;
//        String paymentDescription;
//
//        if (reservation instanceof CarTransfer) {
//            CarTransfer carTransfer = (CarTransfer) reservation;
//            paymentValue = carTransfer.getPrice();
//            paymentDescription = "description" + carTransfer.getId();
//        } else if (reservation instanceof RoomReservation) {
//            RoomReservation roomReservation = (RoomReservation) reservation;
//            paymentValue = roomReservation.getFullPrice();
//            paymentDescription = "description" + roomReservation.getId();
//        } else if (reservation instanceof TableReservation) {
//            TableReservation tableReservation = (TableReservation) reservation;
//            paymentValue = BigDecimal.valueOf(tableReservation.getPrice());
//            paymentDescription = "description" + tableReservation.getId();
//        } else if (reservation instanceof ScreenReservation) {
//            ScreenReservation eventReservation = (ScreenReservation) reservation;
//            paymentValue = BigDecimal.valueOf(eventReservation.getPrice());
//            paymentDescription = "description" + eventReservation.getId();
//        } else {
//            return "Invalid reservation type";
//        }
//
//        String currencyCode = "USD"; //да подавам като параметър или от dto
//
//        //настройване на  параметрите на заявката за създаване на плащане чрез обекта OrdersCreateRequest:
//        //задаване на необходимите данни за плащането, като сума, валута, описание и др .
//        OrdersCreateRequest request = new OrdersCreateRequest();
//        request.prefer("return=representation");
//
//        // Задаване на общите данни за плащането
//        AmountWithBreakdown amount = new AmountWithBreakdown();
//        amount.currencyCode(currencyCode);
//        amount.value(String.valueOf(paymentValue));
//
//        PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest();
//        purchaseUnit.amountWithBreakdown(amount);
//        purchaseUnit.description(paymentDescription);
//
//        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
//        purchaseUnits.add(purchaseUnit);
//
//        request.requestBody(new OrderRequest().checkoutPaymentIntent("CAPTURE").purchaseUnits(purchaseUnits));
//
//
//        try {
//            // Изпращане заявката за създаване на плащане към PayPal чрез payPalHttpClient.execute(request)
//            HttpResponse<Order> response = payPalHttpClient.execute(request);
//
//            // Полученият отговор от PayPal се съхранява в HttpResponse<Order> response.
//            Order createdOrder = response.result();
//
//            // тук ще сменяме PaymentStatus.PAID
//            if (requestDto.getReservationType().equals("CarTransfer")) {
//                CarTransfer carTransfer = carTransferRepository.findById(id).get();
//                carTransferRepository.updateStatusById(PaymentStatus.PAID, id);
//            } else if (requestDto.getReservationType().equals("RoomReservation")) {
//                RoomReservation roomReservation = roomReservationRepository.findById(id).get();
//                roomReservationRepository.updateStatusById(PaymentStatus.PAID, id);
//            } else if (requestDto.getReservationType().equals("TableReservation")) {
//                TableReservation tableReservation = tableReservationRepository.findById(id).get();
//                tableReservationRepository.updateStatusById(PaymentStatus.PAID, id);
//            } else if (requestDto.getReservationType().equals("ScreenReservation")) {
//                ScreenReservation eventReservation = screenReservationRepository.findById(id).get();
//                //screenReservationRepository.updateStatusById(PaymentStatus.PAID, id);
//            }
//
//            //  логика за обработка на отговора от PayPal,
//            //  включваща напр. идентификатора на създаденото плащане от createdOrder
//            return "Payment created: " + createdOrder.id();
//
//        } catch (IOException e) {
//            // Обработка на грешките при изпълнение на заявката
//            e.printStackTrace();
//        }
//
//        return "Error creating payment";
//    }

    // за това имам да чета повече:
//    @PostMapping("/create-card-payment")
//    public String capturePayment() {
//        // Логика за потвърждаване на плащането
//
//        OrdersCaptureRequest request = new OrdersCaptureRequest("<order_id>");
//        // Настройка на параметрите на заявката за потвърждаване на плащането
//        // ...
//
//        try {
//            HttpResponse<Order> response = payPalHttpClient.execute(request);
//            Order capturedOrder = response.result();
//
//            // Обработка на отговора и връщане на резултат
//            // ...
//
//            return "Payment captured: " + capturedOrder.id();
//        } catch (IOException e) {
//            // Обработка на грешките при изпълнение на заявката
//            // ...
//        }
//
//        return "Error capturing payment";
//    }
}


