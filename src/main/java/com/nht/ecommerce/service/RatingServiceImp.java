package com.nht.ecommerce.service;

import com.nht.ecommerce.exception.ProductException;
import com.nht.ecommerce.model.Product;
import com.nht.ecommerce.model.Rating;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.repository.RatingRepository;
import com.nht.ecommerce.request.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImp implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;


    @Override
    public Rating createRating(RatingRequest req, User user) throws Exception {
        Product product = productService.findProductById(req.getProductId());
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        rating.setCreateAt(LocalDateTime.now());

        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductRatings(Long prodcutId) throws ProductException {
//        Product product = productService.findProductById(prodcutId);
        return ratingRepository.getAllProductRatings(prodcutId);
    }
}
