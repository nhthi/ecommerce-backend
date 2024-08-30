package com.nht.ecommerce.service;

import com.nht.ecommerce.exception.ProductException;
import com.nht.ecommerce.model.Review;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.request.ReviewRequest;

import java.util.List;

public interface ReviewService {
    public Review createReview(ReviewRequest req, User user) throws Exception;

    public List<Review> getProductReviews(Long prodcutId) throws ProductException;
}
