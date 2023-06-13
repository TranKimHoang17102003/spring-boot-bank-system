package com.springboot.atm.controller;

import com.springboot.atm.entity.User;
import com.springboot.atm.payload.form.MoneyForm;
import com.springboot.atm.payload.form.TransactionForm;
import com.springboot.atm.repository.UserRepository;
import com.springboot.atm.service.TransactionService;
import com.springboot.atm.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @Autowired
    public TransactionController(TransactionService transactionService, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.userRepository=userRepository;
    }

    @PostMapping("/api/v1/transaction")
    @Operation(summary = "Chuyển khoản")
    public ResponseEntity save(@RequestBody @Valid TransactionForm transactionForm) {
        return new ResponseEntity(transactionService.save(transactionForm), HttpStatus.OK);
    }

    @GetMapping("/api/v1/transaction/collect")
    @Operation(summary = "Xem nhận tiền của tôi")
    public ResponseEntity getViewCashReceipts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with account number " + name));
        return new ResponseEntity(transactionService.getViewCashReceipts(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/api/v1/transaction/transfer")
    @Operation(summary = "Xem giao dịch chuyển đi của tôi")
    public ResponseEntity getViewMoneyTransfers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with account number " + name));
        return new ResponseEntity(transactionService.getViewMoneyTransfers(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/api/v1/transaction/admin/collect/{id}")
    @Operation(summary = "Xem giao dịch nhận tiền của 1 USER <ADMIN>")
    public ResponseEntity getViewCashReceiptsOfUserId(@PathVariable("id") Long id) {
        return new ResponseEntity(transactionService.getViewCashReceipts(id), HttpStatus.OK);
    }

    @GetMapping("/api/v1/transaction/admin/transfer/{id}")
    @Operation(summary = "Xem giao dịch chuyển tiền của 1 USER <ADMIN>")
    public ResponseEntity getViewMoneyTransfersOfUserId(@PathVariable("id") Long id) {
        return new ResponseEntity(transactionService.getViewMoneyTransfers(id), HttpStatus.OK);
    }

    @GetMapping("/api/v1/transaction/admin/get-all")
    @Operation(summary = "Xem tất cả giao dịch <ADMIN>")
    public ResponseEntity getAll(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER , required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE , required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY , required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION , required = false) String sortDir
    ) {
        return new ResponseEntity(transactionService.getAll(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/api/v1/transaction/admin/get-all/{id}")
    @Operation(summary = "Xem tất cả giao dịch của USER bằng id <ADMIN>")
    public ResponseEntity getAll(@PathVariable("id") Long userId) {
        return new ResponseEntity(transactionService.getAllByUserId(userId), HttpStatus.OK);
    }

    @PostMapping("/api/v1/transaction/deposit")
    @Operation(summary = "Nạp tiền")
    public ResponseEntity deposit(@RequestBody @Valid MoneyForm moneyForm) {
        return new ResponseEntity(transactionService.deposit(moneyForm), HttpStatus.OK);
    }

    @PostMapping("/api/v1/transaction/withdraw")
    @Operation(summary = "Rút tiền")
    public ResponseEntity withdraw(@RequestBody @Valid MoneyForm moneyForm) {
        return new ResponseEntity(transactionService.withdraw(moneyForm), HttpStatus.OK);
    }
}