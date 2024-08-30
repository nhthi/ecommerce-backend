package com.nht.ecommerce.service;

import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.Cart;
import com.nht.ecommerce.model.CartItem;
import com.nht.ecommerce.model.Product;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.request.AddItemRequest;

public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);
    public CartItem updateCartItemQuantity(Long cartItemId,int quantity) throws Exception, UserException;
    public CartItem isCartItemExist(Cart cart, Product product,String size, Long userId) throws Exception;
    public Cart removeItemFromCart(Long cartItemId, Long userId) throws Exception;
    public CartItem findCartItemById(Long cartItemId) throws Exception;
    public CartItem addCartItem(Long userId, AddItemRequest req) throws Exception;
}
