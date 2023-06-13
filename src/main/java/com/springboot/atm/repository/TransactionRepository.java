package com.springboot.atm.repository;

import com.springboot.atm.entity.Transaction;
import com.springboot.atm.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "select * from transaction where receiver_id= :id", nativeQuery = true)
    List<Transaction> findAllByReceiverId(Long id); //nguoi nhan


    @Query(value = "select * from transaction where remitter_id= :id", nativeQuery = true)
    List<Transaction> findAllByRemitterId(Long id); //nguoi chuyen

    @Query(value = "select * from transaction where remitter_id= :id or receiver_id= :id", nativeQuery = true)
    List<Transaction> findAllByUserId(Long id);

    Page<Transaction> findAll(Pageable pageable);
}
