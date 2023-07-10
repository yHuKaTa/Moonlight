package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.dto.payment.PaymentRequestDto;
import com.aacdemy.moonlight.dto.payment.PaymentValueAndDescriptionDto;
import com.aacdemy.moonlight.entity.PaymentStatus;
import com.aacdemy.moonlight.entity.car.CarTransfer;
import com.aacdemy.moonlight.entity.hotel.RoomReservation;
import com.aacdemy.moonlight.entity.restaurant.TableReservation;
import com.aacdemy.moonlight.entity.screen.ScreenReservation;
import com.aacdemy.moonlight.exception.EntityNotFoundException;
import com.aacdemy.moonlight.repository.car.CarTransferRepository;
import com.aacdemy.moonlight.repository.hotel.ReservationRepository;
import com.aacdemy.moonlight.repository.restaurant.TableReservationRepository;
import com.aacdemy.moonlight.repository.screen.ScreenReservationRepository;
import com.aacdemy.moonlight.service.PaymentService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.orders.Order;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// да се добавят проверки дали вече е извършено плащането, дали валутата е подходяща и т.н.
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PayPalHttpClient payPalHttpClient;
    private final CarTransferRepository carTransferRepository;
    private final ReservationRepository roomReservationRepository;
    private final TableReservationRepository tableReservationRepository;
    private final ScreenReservationRepository screenReservationRepository;

    public PaymentServiceImpl(PayPalHttpClient payPalHttpClient,
                              CarTransferRepository carTransferRepository,
                              ReservationRepository roomReservationRepository,
                              TableReservationRepository tableReservationRepository,
                              ScreenReservationRepository screenReservationRepository) {
        this.payPalHttpClient = payPalHttpClient;
        this.carTransferRepository = carTransferRepository;
        this.roomReservationRepository = roomReservationRepository;
        this.tableReservationRepository = tableReservationRepository;
        this.screenReservationRepository = screenReservationRepository;
    }

    // Плащания имаме за:
    // CarTransfer, RoomReservation, TableReservation, EventReservation
    String carTransfer = "CarTransfer";
    String roomReservation = "RoomReservation";
    String tableReservation = "TableReservation";
    String eventReservation = "EventReservation";
    String errorMsgPossibleReservations = "Invalid reservation type. Possible values: "
            + carTransfer + ", " + roomReservation + ", " + tableReservation + ", " + eventReservation;

    // В метода createPayment() се извършва логиката за създаване на плащане с PayPal.
    //{
    //    "reservationType": "RoomReservation",
    //    "id": "1"
    //}
    @Override
    public Map<String, URL> createPayment(PaymentRequestDto requestDto) {

        //ЛОГИКА ЗА ПРОВЕРКА НА НЕОБХОДИМОСТ ОТ ПЛАЩАНЕ

        Object reservation = getReservationObject(requestDto);
        Long id = requestDto.getId();

        //задаване на необходимите данни за плащането, като сума, валута, описание и др .
        BigDecimal paymentValue = getPaymentValueAndDescription(reservation).getPaymentValue();
        String paymentDescription = getPaymentValueAndDescription(reservation).getPaymentDescription();
        String currencyCode = "EUR"; // не работи с BGN, виж документацията на paypal

        //настройване на  параметрите на заявката за създаване на плащане чрез обекта OrdersCreateRequest:
        OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");

        // Задаване на общите данни за плащането
        AmountWithBreakdown amount = new AmountWithBreakdown();
        amount.currencyCode(currencyCode);
        amount.value(String.valueOf(paymentValue));

        PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest();
        purchaseUnit.amountWithBreakdown(amount);
        purchaseUnit.description(paymentDescription);

        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(purchaseUnit);

        request.requestBody(new OrderRequest()
                .checkoutPaymentIntent("CAPTURE")
                .purchaseUnits(purchaseUnits));

        try {
            // Изпращане заявката за създаване на плащане към PayPal чрез payPalHttpClient.execute(request)
            HttpResponse<Order> response = payPalHttpClient.execute(request);

            // Полученият отговор от PayPal се съхранява в HttpResponse<Order> response.
            Order createdOrder = response.result();

            String approvalURL = getApprovalUrl(createdOrder);
            System.out.println(approvalURL);

            // сменяме в таблиците статуса на платен
            changePaymentStatusToPAID(requestDto);

            //  логика за обработка на отговора от PayPal,
//            return "Payment created: " + createdOrder.id()
//                    + " " + createdOrder.status();  //  включваща напр. идентификатора на създаденото плащане от createdOrder
            Map<String, URL> result = new HashMap<>();
            result.put(createdOrder.id(), new URL(approvalURL));
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, URL> result = new HashMap<>();
        result.put("Error creating payment", null);
        return result;
    }

    private static String getApprovalUrl(Order createdOrder) {
        List<LinkDescription> links = createdOrder.links();
        String approvalUrl = "";
        for (LinkDescription link : links) {
            if (link.rel().equals("approve")) {
                approvalUrl = link.href();
                break;
            }
        }

        if (!approvalUrl.isEmpty()) {
            return approvalUrl;
        } else {
            return "Approval URL not found";
        }
    }

    public String authorizePayment(String createdOrderId) {
        try {

            OrdersAuthorizeRequest authorizeRequest = new OrdersAuthorizeRequest(createdOrderId);
            authorizeRequest.header("PayPal-Request-Id", "AUTH-" + createdOrderId);
            authorizeRequest.requestBody(new AuthorizeRequest());

            // Изпращане на заявката за одобряване на поръчката
            HttpResponse<Order> authorizeResponse = payPalHttpClient.execute(authorizeRequest);

            // Получаване на одобрената поръчка
            Order authorizedOrder = authorizeResponse.result();

            // Проверка на статуса на одобрената поръчка
            if (authorizedOrder.status().equals("COMPLETED")) {
                // Плащането е успешно одобрено
                System.out.println("Payment approved: " + authorizedOrder.id());
            } else {
                // Плащането не е било одобрено
                System.out.println("Payment approval failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

////approval code
//      try {
//            OrdersAuthorizeRequest authorizeRequest = new OrdersAuthorizeRequest(createdOrderId);
//
//
//            authorizedOrder = payPalHttpClient.execute(authorizeRequest).result();
//
//            // Проверка на статуса на одобреното плащане
//            if (authorizedOrder.status().equals("COMPLETED")) {
//                // Плащането е успешно одобрено
//                System.out.println("Payment approved: " + authorizedOrder.id());
//                return authorizedOrder.id();
//            } else {
//                // Плащането не е било одобрено
//                System.out.println("Payment approval failed");
//            }
//            String approvalUrl = "";
//
//// Извличане на "approve" URL от authorizedOrder.links()
//            List<LinkDescription> links = authorizedOrder.links();
//            for (LinkDescription link : links) {
//                if (link.rel().equals("approve")) {
//                    approvalUrl = link.href();
//                    break;
//                }
//            }
//
//// Пренасочване на потребителя към "approve" URL
//            if (!approvalUrl.isEmpty()) {
//                // Вашата логика за пренасочване на потребителя към approvalUrl
//            } else {
//                System.out.println("Approval URL not found");
//            }
//
//        } catch (IOException e) {
//            System.out.println("authorizePayment:");
//            e.printStackTrace();
//        }
//
        // Връщане на грешка при улавянето на плащането
        return "Error approve payment";

    }


    //confirm, authorize, capture
    public String executePayment(String createdOrderId) {


        try {
            // Изпращане на заявка за улавяне на плащането към PayPal
            Order orderToCapture = payPalHttpClient.execute(
                    new OrdersCaptureRequest(createdOrderId)).result();

            // Проверка дали плащането е успешно улавено
            if (orderToCapture != null && orderToCapture.status().equals("COMPLETED")) {
                // Плащането е успешно улавено
                // Изпълнете допълнителна логика тук, ако е необходимо

                return "Payment captured successfully";
            } else {
                // Плащането не е успешно улавено
                // Изпълнете допълнителна логика тук, ако е необходимо

                return "Payment capture failed";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Връщане на грешка при улавянето на плащането
        return "Error capturing payment";
    }


    //{
    //  "reservationType": "RoomReservation",
    //  "id": 12345,
    //  "cardNumber": "************1234",
    //  "expirationDate": "12/25",
    //  "cvv": "123"
    //}
//    @Override
//    public String createCardPayment(PaymentCardRequestDto requestDto) {
//
//        Object reservation = getReservationObject(requestDto);
//        Long id = requestDto.getId();
//
//        //задаване на необходимите данни за плащането, като сума, валута, описание и др .
//        BigDecimal paymentValue = getPaymentValueAndDescription(reservation).getPaymentValue();
//        String paymentDescription = getPaymentValueAndDescription(reservation).getPaymentDescription();
//        String currencyCode = "USD"; // не работи с BGN, виж документацията на paypal
//
//// Създаване на обект от тип CreditCard
//        CreditCard creditCard = new CreditCard();
//        creditCard.setNumber("4111111111111111"); // Номер на кредитната карта
//        creditCard.setType("visa"); // Тип на кредитната карта
//        creditCard.setExpireMonth(12); // Месец на изтичане на карта
//        creditCard.setExpireYear(2023); // Година на изтичане на карта
//        creditCard.setCvv2("123"); // CVV код на карта
//
//// Създаване на обект от тип FundingInstrument и задаване на CreditCard обекта
//        FundingInstrument fundingInstrument = new FundingInstrument();
//        fundingInstrument.setCreditCard(creditCard);
//
//// Създаване на обект от тип Payer и задаване на FundingInstrument обекта
//        Payer payer = new Payer();
//        payer.setPaymentMethod("credit_card");
//        payer.setFundingInstruments(Collections.singletonList(fundingInstrument));
//
//// Задаване на сума, валута и други детайли за плащането
//        Amount amount = new Amount();
//        amount.setTotal("100.00"); // Общата сума на плащането
//        amount.setCurrency("USD"); // Валутата на плащането
//
//// Създаване на обект от тип Transaction и задаване на сумата
//        Transaction transaction = new Transaction();
//        transaction.setAmount(amount);
//
//// Създаване на обект от тип Payment и задаване на Payer и Transaction обектите
//        Payment payment = new Payment();
//        payment.setIntent("sale"); // Интент на плащането
//        payment.setPayer(payer);
//        payment.setTransactions(Collections.singletonList(transaction));
//
//
//        try {
//
//            PaymentCreateRequest paymentCreateRequest = new PaymentCreateRequest();
//            paymentCreateRequest.requestBody(payment);
//            HttpResponse<Payment> response = payPalHttpClient.execute(new PaymentCreateRequest(payment));
//
//// Обработка на отговора от PayPal
//            if (response.statusCode() == 201) {
//                Payment createdPayment = response.result();
//
//
//                // сменяме в таблиците статуса на платен
//                changePaymentStatusToPAID(requestDto);
//
//                return "Payment created: " + createdOrder.id();
//
//
//            } else {
//                String errorDetails = response.result().getFailureReason();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return "Error creating payment";
//    }


    // Разпознаване на обекта
    public Object getReservationObject(PaymentRequestDto requestDto) {

        Object reservation;
        Long id = requestDto.getId();

        if (requestDto.getReservationType().equals(carTransfer)) {
            reservation = carTransferRepository.findById(id).orElse(null);
        } else if (requestDto.getReservationType().equals(roomReservation)) {
            reservation = roomReservationRepository.findById(id).orElse(null);
        } else if (requestDto.getReservationType().equals(tableReservation)) {
            reservation = tableReservationRepository.findById(id).orElse(null);
        } else if (requestDto.getReservationType().equals(eventReservation)) {
            reservation = screenReservationRepository.findById(id).orElse(null);
        } else {
            return errorMsgPossibleReservations;
        }

        if (reservation == null) {
            return "Reservation not found";
        }

        return reservation;
    }


    // тук ще сменяме в базата данни на PaymentStatus.PAID
    public void changePaymentStatusToPAID(PaymentRequestDto requestDto) {
        Long id = requestDto.getId();
        if (requestDto.getReservationType().equals("CarTransfer")) {
            CarTransfer carTransfer = carTransferRepository.findById(id).get();
            carTransferRepository.updateStatusById(PaymentStatus.PAID, id);
        } else if (requestDto.getReservationType().equals("RoomReservation")) {
            RoomReservation roomReservation = roomReservationRepository.findById(id).get();
            roomReservationRepository.updateStatusById(PaymentStatus.PAID, id);
        } else if (requestDto.getReservationType().equals("TableReservation")) {
            TableReservation tableReservation = tableReservationRepository.findById(id).get();
            tableReservationRepository.updateStatusById(PaymentStatus.PAID, id);
        } else if (requestDto.getReservationType().equals("ScreenReservation")) {
            ScreenReservation eventReservation = screenReservationRepository.findById(id).get();
            //screenReservationRepository.updateStatusById(PaymentStatus.PAID, id);
        }
    }

    public PaymentValueAndDescriptionDto getPaymentValueAndDescription(Object reservation) {

        BigDecimal paymentValue = new BigDecimal(0);
        String paymentDescription = "Invalid reservation type for payment description";

        if (reservation instanceof CarTransfer) {
            CarTransfer carTransfer = (CarTransfer) reservation;
            paymentValue = carTransfer.getPrice();
            paymentDescription = "description" + carTransfer.getId();
        } else if (reservation instanceof RoomReservation) {
            RoomReservation roomReservation = (RoomReservation) reservation;
            paymentValue = roomReservation.getFullPrice();
            paymentDescription = "description" + roomReservation.getId();
        } else if (reservation instanceof TableReservation) {
            TableReservation tableReservation = (TableReservation) reservation;
            paymentValue = BigDecimal.valueOf(tableReservation.getPrice());
            paymentDescription = "description" + tableReservation.getId();
        } else if (reservation instanceof ScreenReservation) {
            ScreenReservation eventReservation = (ScreenReservation) reservation;
            paymentValue = BigDecimal.valueOf(eventReservation.getPrice());
            paymentDescription = "description" + eventReservation.getId();
        } else {
            throw new EntityNotFoundException("Invalid reservation type");
        }

        return PaymentValueAndDescriptionDto.builder()
                .paymentValue(paymentValue)
                .paymentDescription(paymentDescription)
                .build();
    }


}
