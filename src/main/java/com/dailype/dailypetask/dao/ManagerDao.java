package com.dailype.dailypetask.dao;

import com.dailype.dailypetask.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ManagerDao  extends JpaRepository<Manager,String> {
}
