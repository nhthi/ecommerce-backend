package com.nht.ecommerce.controller;

import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.CartItem;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.request.UpdateCartItemRequest;
import com.nht.ecommerce.response.ApiResponse;
import com.nht.ecommerce.service.CartItemService;
import com.nht.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-item")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@RequestHeader("Authorization")String jwt, @PathVariable("cartItemId")Long cartItemId) throws Exception {
        User user = userService.findUserByJwt(jwt);
        cartItemService.removeItemFromCart(cartItemId,user.getId());
        ApiResponse apiResponse =new ApiResponse("delete item from cart, id :"+cartItemId,true);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<CartItem> updateCartItem (@RequestBody UpdateCartItemRequest req) throws Exception {
        CartItem updateCartItem = cartItemService.updateCartItemQuantity(req.getCartItemId(), req.getQuantity());
        return new ResponseEntity<>(updateCartItem, HttpStatus.OK);
    }
}
