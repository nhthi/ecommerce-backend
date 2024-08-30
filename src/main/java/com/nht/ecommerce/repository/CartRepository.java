package com.nht.ecommerce.repository;

import com.nht.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    public Cart findByUserId(Long userId);
}
