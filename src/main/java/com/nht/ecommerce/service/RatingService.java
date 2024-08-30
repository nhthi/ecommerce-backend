package com.nht.ecommerce.service;

import com.nht.ecommerce.exception.ProductException;
import com.nht.ecommerce.model.Rating;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.request.RatingRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingService {
    public Rating createRating(RatingRequest req, User user) throws Exception;

    public List<Rating> getProductRatings(Long prodcutId) throws ProductException;
}
