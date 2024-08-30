package com.nht.ecommerce.controller;


import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.Cart;
import com.nht.ecommerce.model.CartItem;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.request.AddItemRequest;
import com.nht.ecommerce.response.ApiResponse;
import com.nht.ecommerce.service.CartItemService;
import com.nht.ecommerce.service.CartService;
import com.nht.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestHeader("Authorization")String jwt, @RequestBody AddItemRequest req) throws Exception {
        User user = userService.findUserByJwt(jwt);
        CartItem cartItem = cartItemService.addCartItem(user.getId(),req);
//        ApiResponse res = new ApiResponse("item added to cart",true);
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }
}
