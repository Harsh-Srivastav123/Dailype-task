package com.dailype.dailypetask.dao;

import com.dailype.dailypetask.entity.RefreshToken;
import com.dailype.dailypetask.entity.UserSecured;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken, Integer> {
     RefreshToken findByToken(String refreshToken);

    boolean existsByToken(String refreshToken);
    RefreshToken findByUserSecured(UserSecured userSecured);
}
