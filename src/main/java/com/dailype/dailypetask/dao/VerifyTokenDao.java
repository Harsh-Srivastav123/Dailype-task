package com.dailype.dailypetask.dao;

import com.dailype.dailypetask.entity.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyTokenDao extends JpaRepository<VerifyToken,Integer> {
    VerifyToken findByToken(String token);

    boolean existsByToken(String refreshToken);
}
