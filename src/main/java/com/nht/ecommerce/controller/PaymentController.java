package com.nht.ecommerce.controller;

import com.nht.ecommerce.exception.OrderException;
import com.nht.ecommerce.model.Order;
import com.nht.ecommerce.repository.OrderRepository;
import com.nht.ecommerce.response.PaymentResponse;
import com.nht.ecommerce.service.OrderService;
import com.nht.ecommerce.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;


    @PostMapping("/payment/{orderId}")
    public ResponseEntity<PaymentResponse> createPaymentLink(@PathVariable("orderId") Long orderId, @RequestHeader("Authorization")String jwt) throws StripeException, OrderException {
        Order order = orderService.findOrderById(orderId);

        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(
                        SessionCreateParams.PaymentMethodType.CARD
                ).setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment/success/"+order.getId())
                .setCancelUrl("http://localhost:3000/payment/failure")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long)order.getTotalPrice()*100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("NHT")
                                        .build())
                                .build()
                        ).build()
                ).build();

        Session session = Session.create(params);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_link_url(session.getUrl());
        res.setPayment_link_id((session.getId()));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
