package com.dailype.dailypetask.dao;

import com.dailype.dailypetask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User,String> {
    @Query(value = "SELECT * FROM user WHERE manager_id= :id",nativeQuery = true)
    List<User> findByManagerId(@Param("id") String manager_id);

    @Query(value = "SELECT * FROM user WHERE mob_num= :num LIMIT 1",nativeQuery = true)
    User findByMobNum(@Param("num") String mobile_num);

    boolean existsByMobNum(String mobNum);
}
