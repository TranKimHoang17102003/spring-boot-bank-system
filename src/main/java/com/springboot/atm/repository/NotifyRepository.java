package com.springboot.atm.repository;

import com.springboot.atm.entity.Notify;
import com.springboot.atm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
    List<Notify> findAllByUser(User user);

    @Query(value = "select * from notify where user_id= :userId", nativeQuery = true)
    List<Notify> findAllByUserId(Long userId);
}
