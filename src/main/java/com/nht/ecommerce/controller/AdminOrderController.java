package com.nht.ecommerce.controller;

import com.nht.ecommerce.exception.OrderException;
import com.nht.ecommerce.model.Order;
import com.nht.ecommerce.response.ApiResponse;
import com.nht.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrderHandler(){
        List<Order> orders = orderService.getAllOrder();
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<Order> confirmedOrderHandler(@PathVariable("orderId") Long orderId,
                                                       @RequestHeader("Authorization")String jwt) throws OrderException {
            Order order = orderService.confirmedOrder(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @PutMapping("/{orderId}/place")
    public ResponseEntity<Order> placedOrderHandler(@PathVariable("orderId") Long orderId,
                                                       @RequestHeader("Authorization")String jwt) throws OrderException {
        Order order = orderService.placedOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shippedOrderHandler(@PathVariable("orderId") Long orderId,
                                                       @RequestHeader("Authorization")String jwt) throws OrderException {
        Order order = orderService.shippedOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/delivery")
    public ResponseEntity<Order> deliveredOrderHandler(@PathVariable("orderId") Long orderId,
                                                     @RequestHeader("Authorization")String jwt) throws OrderException {
        Order order = orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelledOrderHandler(@PathVariable("orderId") Long orderId,
                                                       @RequestHeader("Authorization")String jwt) throws OrderException {
        Order order = orderService.cancelledOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable("orderId") Long orderId,
                                                       @RequestHeader("Authorization")String jwt) throws OrderException {
        orderService.deleteOrder(orderId);
        ApiResponse res = new ApiResponse();
        res.setMessage("order deleted successfully, id: "+orderId);
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
