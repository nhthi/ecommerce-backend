package com.nht.ecommerce.service;


import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.Cart;
import com.nht.ecommerce.model.CartItem;
import com.nht.ecommerce.model.Product;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.repository.CartItemRepository;
import com.nht.ecommerce.repository.CartRepository;
import com.nht.ecommerce.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImp implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductService productService;

    @Override
    public CartItem createCartItem(CartItem cartItem) {

        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        cartItem.setDiscountedPrice(cartItem.getDiscountedPrice() * cartItem.getQuantity());
        CartItem createdCartItem = cartItemRepository.save(cartItem);

        return createdCartItem;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isEmpty()) {
            throw new Exception("Cart item not found with id :" + cartItemId);
        }
        CartItem item = cartItem.get();
        item.setQuantity(quantity);
        item.setTotalPrice(quantity * item.getProduct().getPrice());
        item.setTotalPrice(quantity * item.getProduct().getDiscountPrice());
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem addCartItem(Long userId, AddItemRequest req) throws Exception {
        Cart cart = cartRepository.findByUserId(userId);
        User user = userService.findUserById(userId);
        Product product = productService.findProductById(req.getProductId());
        CartItem addItem = new CartItem();
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getProduct().equals(product)) {
                int newQuantity = cartItem.getQuantity() + req.getQuantity();
                CartItem upCart =  updateCartItemQuantity(cartItem.getId(), newQuantity);
                cart.setTotalPrice(calculateCartTotals(cart));
                cartRepository.save(cart);
                return upCart;
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setSize(req.getSize());
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setPrice(product.getPrice());
        newCartItem.setDiscountedPrice(product.getDiscountPrice());
        newCartItem.setCart(cart);
        newCartItem.setTotalPrice(req.getQuantity() * product.getDiscountPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        cart.getCartItems().add(savedCartItem);
        cart.setTotalPrice(calculateCartTotals(cart));
        cartRepository.save(cart);

        return savedCartItem;
    }
    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) throws Exception {
        return null;
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, Long userId) throws Exception {
        User user = userService.findUserById(userId);
        Cart cart = cartRepository.findByUserId(userId);

        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isEmpty()) {
            throw new Exception("Cart item not found with id :" + cartItemId);
        }
        CartItem item = cartItem.get();
        cart.getCartItems().remove(item);
        cart.setTotalPrice(calculateCartTotals(cart));

        return cartRepository.save(cart);
    }


    @Override
    public CartItem findCartItemById(Long cartItemId) throws Exception {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isEmpty()) {
            throw new Exception("Cart item not found with id :" + cartItemId);
        }
        return cartItem.get();
    }


    public Long calculateCartTotals(Cart cart) throws Exception {
        Long total = 0L;
        for (CartItem cartItem : cart.getCartItems()) {
            total += cartItem.getTotalPrice();
        }
        return total;
    }
}
