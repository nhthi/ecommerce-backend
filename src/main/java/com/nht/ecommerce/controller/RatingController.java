package com.nht.ecommerce.controller;


import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.Rating;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.request.RatingRequest;
import com.nht.ecommerce.service.RatingService;
import com.nht.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestHeader("Authorization")String jwt, @RequestBody RatingRequest req) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Rating rating = ratingService.createRating(req,user);
        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<List<Rating>> getProductRatings(@RequestHeader("Authorization")String jwt, @PathVariable("productId") Long productId) throws Exception {
        User user = userService.findUserByJwt(jwt);
        List<Rating> ratings = ratingService.getProductRatings(productId);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }


}
