package com.nht.ecommerce.controller;

import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.Address;
import com.nht.ecommerce.model.Order;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.service.OrderService;
import com.nht.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress,
                                             @RequestHeader("Authorization")String jwt) throws Exception {
        User user  = userService.findUserByJwt(jwt);
        Order order = orderService.createOrder(user,shippingAddress);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Order> findOrderById(@RequestHeader("Authorization")String jwt,@PathVariable("orderId")Long orderId) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}

