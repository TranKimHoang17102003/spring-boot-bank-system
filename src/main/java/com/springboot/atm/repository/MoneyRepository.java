package com.springboot.atm.repository;

import com.springboot.atm.entity.Money;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyRepository extends JpaRepository<Money, Long> {
}
