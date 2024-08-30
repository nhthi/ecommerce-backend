package com.nht.ecommerce.repository;

import com.nht.ecommerce.model.Verify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyRepository extends JpaRepository<Verify, Long> {
    public Verify findByEmail(String email);
}
