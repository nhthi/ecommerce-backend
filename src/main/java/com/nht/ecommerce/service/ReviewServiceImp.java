package com.nht.ecommerce.service;

import com.nht.ecommerce.exception.ProductException;
import com.nht.ecommerce.model.Product;
import com.nht.ecommerce.model.Review;
import com.nht.ecommerce.model.User;
import com.nht.ecommerce.repository.ReviewRepository;
import com.nht.ecommerce.request.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ReviewServiceImp implements ReviewService{



    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductService productService;



    @Override
    public Review createReview(ReviewRequest req, User user) throws Exception {
        Product product = productService.findProductById(req.getProductId());
        Review rv = new Review();
        rv.setProduct(product);
        rv.setUser(user);
        rv.setContent(req.getContent());
        rv.setCreateAt(LocalDateTime.now());

        return reviewRepository.save(rv);
    }

    @Override
    public List<Review> getProductReviews(Long prodcutId) throws ProductException {
//        Product product = productService.findProductById(prodcutId);
        return reviewRepository.getAllProductReviews(prodcutId);
    }
}
