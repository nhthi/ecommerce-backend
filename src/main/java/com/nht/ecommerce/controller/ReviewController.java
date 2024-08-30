package com.nht.ecommerce.controller;


import com.nht.ecommerce.exception.UserException;
import com.nht.ecommerce.model.Review;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.request.ReviewRequest;
import com.nht.ecommerce.service.ReviewService;
import com.nht.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestHeader("Authorization")String jwt, @RequestBody ReviewRequest req) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Review review = reviewService.createReview(req,user);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<List<Review>> getProductReviews(@RequestHeader("Authorization")String jwt, @PathVariable("productId") Long productId) throws Exception {
        User user = userService.findUserByJwt(jwt);
        List<Review> reviews = reviewService.getProductReviews(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

}
