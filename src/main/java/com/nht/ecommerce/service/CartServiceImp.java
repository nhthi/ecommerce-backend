package com.nht.ecommerce.service;

import com.nht.ecommerce.model.Cart;
import com.nht.ecommerce.model.CartItem;
import com.nht.ecommerce.model.Product;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.repository.CartItemRepository;
import com.nht.ecommerce.repository.CartRepository;
import com.nht.ecommerce.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }



    public Long calculateCartTotals(Cart cart) throws Exception {
        Long total = 0L;
        for (CartItem cartItem : cart.getCartItems()) {
            total += cartItem.getTotalPrice();
        }
        return total;
    }

    @Override
    public Cart findUserCart(Long userId) throws Exception {
        Cart cart = cartRepository.findByUserId(userId);
        cart.setTotalPrice(calculateCartTotals(cart));
        return cartRepository.save(cart);
    }
}
