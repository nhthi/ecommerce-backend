package com.nht.ecommerce.service;

import com.nht.ecommerce.exception.OrderException;
import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.Address;
import com.nht.ecommerce.model.Order;
import com.nht.ecommerce.model.User;

import java.util.List;

public interface OrderService {
    public Order findOrderById(Long orderId) throws OrderException;

    public Order createOrder(User user, Address shippingAddress) throws Exception;

    public List<Order> usersOrderHistory(Long userId) throws UserException;

    public Order placedOrder(Long orderId) throws OrderException;

    public Order confirmedOrder(Long orderId) throws OrderException;

    public Order shippedOrder(Long orderId) throws OrderException;

    public Order deliveredOrder(Long orderId) throws OrderException;

    public Order cancelledOrder(Long orderId) throws OrderException;

    public List<Order> getAllOrder();

    public void deleteOrder(Long orderId) throws OrderException;

}
