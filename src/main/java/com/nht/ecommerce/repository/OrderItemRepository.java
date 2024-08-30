package com.nht.ecommerce.repository;

import com.nht.ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    public void deleteByProductId(Long productId);
}
