package com.nht.ecommerce.service;

import com.nht.ecommerce.model.Cart;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.request.AddItemRequest;

public interface CartService {
    public Cart createCart(User user);


    public Cart findUserCart(Long userId) throws Exception;
}
