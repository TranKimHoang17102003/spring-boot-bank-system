package com.springboot.atm.service;

import com.springboot.atm.payload.dto.MoneyDto;
import com.springboot.atm.payload.dto.TransactionDto;
import com.springboot.atm.payload.dto.TransactionListResponse;
import com.springboot.atm.payload.dto.UserDto;
import com.springboot.atm.payload.form.MoneyForm;
import com.springboot.atm.payload.form.TransactionForm;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface TransactionService{
    public TransactionDto save(TransactionForm transactionForm);
    public TransactionDto getById(Long id);
    public List<TransactionDto> getViewCashReceipts(Long id); //xem giao dich nhan
    public List<TransactionDto> getViewMoneyTransfers(Long id); //xem giao dich chuyen di
    public MoneyDto deposit(MoneyForm moneyForm);
    public MoneyDto withdraw(MoneyForm moneyForm);
    public TransactionListResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    public List<TransactionDto> getAllByUserId(Long userId);
}
