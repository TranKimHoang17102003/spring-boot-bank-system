package com.springboot.atm.service.impl;

import com.springboot.atm.entity.Notify;
import com.springboot.atm.entity.Transaction;
import com.springboot.atm.entity.User;
import com.springboot.atm.exception.AtmAPIException;
import com.springboot.atm.payload.dto.*;
import com.springboot.atm.payload.form.MoneyForm;
import com.springboot.atm.payload.form.TransactionForm;
import com.springboot.atm.repository.NotifyRepository;
import com.springboot.atm.repository.TransactionRepository;
import com.springboot.atm.repository.UserRepository;
import com.springboot.atm.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final NotifyRepository notifyRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, ModelMapper mapper, NotifyRepository notifyRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.mapper=mapper;
        this.notifyRepository = notifyRepository;
    }

    @Override
    public TransactionDto save(TransactionForm transactionForm) {
        Transaction transaction = mapToEntity(transactionForm);
        Double number = transaction.getNumber();

        Double currentAmountRemitter = transaction.getRemitter().getMoney().getCurrentAmount();
        Double currentAmountReceiver = transaction.getReceiver().getMoney().getCurrentAmount();

        if (transaction.getNumber() <= 0) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Number must than 0 vnd!");
        } else if (currentAmountRemitter < number) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Insufficient account balance!");
        }

        //nguoi gui
        transaction.getRemitter().getMoney().setCurrentAmount(currentAmountRemitter - number);
        //nguoi nhan
        transaction.getReceiver().getMoney().setCurrentAmount(currentAmountReceiver + number);

        //tao thong bao nap tien
        Notify notify=new Notify();
        notify.setUser(transaction.getReceiver());
        notify.setTitle("Nhan tien");
        notify.setContent("Ban da duoc nhan " + number
                + " vnd vao tai khoan tu stk: "
                + transaction.getRemitter().getAccountNumber()
                + " - " + transaction.getRemitter().getLastName() + " "
                + transaction.getRemitter().getFirstName()
                + ". So du hien tai la " + transaction.getReceiver().getMoney().getCurrentAmount() + " vnd");
        notify.setNote(transaction.getNote());

        notifyRepository.save(notify);

        transactionRepository.save(transaction);
        return mapToDto(transaction);
    }

    @Override
    public TransactionDto getById(Long id) {
        return null;
    }

    @Override
    public List<TransactionDto> getViewMoneyTransfers(Long id) { //xem giao dich chuyển
        return transactionRepository.findAllByRemitterId(id).stream().map(transaction -> mapToDto(transaction)).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> getViewCashReceipts(Long id) {//xem giao dich chuyển nhan
        return transactionRepository.findAllByReceiverId(id).stream().map(transaction -> mapToDto(transaction)).collect(Collectors.toList());
    }

    //nap tien
    @Override
    public MoneyDto deposit(MoneyForm moneyForm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with account number " + name));

        if(!user.getAccountNumber().equals(moneyForm.getAccountNumber())) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Account number is not true!");
        } else if (!user.getPin().equals(moneyForm.getPin())) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Pin is not true!");
        } else if (moneyForm.getNumber()< 50000) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Number must than 50.000 vnd!");
        }

        Double number = user.getMoney().getCurrentAmount() + moneyForm.getNumber();
        user.getMoney().setCurrentAmount(number);

        //tao thong bao nap tien
        Notify notify=new Notify();
        notify.setUser(user);
        notify.setTitle("Nap tien");
        notify.setContent("Ban da nap " + moneyForm.getNumber() + " vnd vao tai khoan. So du hien tai la " + user.getMoney().getCurrentAmount() + " vnd");
        notifyRepository.save(notify);

        userRepository.save(user);
        return new MoneyDto(moneyForm.getNumber(), user.getLastName() + " " + user.getFirstName(), user.getAccountNumber(), user.getBank());
    }

    //rut tien
    @Override
    public MoneyDto withdraw(MoneyForm moneyForm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with account number " + name));

        if(!user.getAccountNumber().equals(moneyForm.getAccountNumber())) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Account number is not true!");
        } else if (!user.getPin().equals(moneyForm.getPin())) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Pin is not true!");
        } else if (user.getMoney().getCurrentAmount() < moneyForm.getNumber()) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Insufficient account balance!");
        } else if (moneyForm.getNumber()< 50000) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Number must than 50.000 vnd!");
        }

        Double number = user.getMoney().getCurrentAmount() - moneyForm.getNumber();
        user.getMoney().setCurrentAmount(number);

        //tao thong bao nap tien
        Notify notify=new Notify();
        notify.setUser(user);
        notify.setTitle("Rut tien");
        notify.setContent("Ban da rut " + moneyForm.getNumber() + " vnd tu tai khoan. So du hien tai la " + user.getMoney().getCurrentAmount() + " vnd");
        notifyRepository.save(notify);

        userRepository.save(user);
        return new MoneyDto(moneyForm.getNumber(), user.getLastName() + " " + user.getFirstName(), user.getAccountNumber(), user.getBank());
    }

    @Override
    public TransactionListResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equals(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Transaction> listTransactionPage = transactionRepository.findAll(pageable);

        List<Transaction> listTransaction = listTransactionPage.getContent();

        List<TransactionDto> content = listTransaction.stream().map(transaction -> mapToDto(transaction)).collect(Collectors.toList());

        TransactionListResponse transactionListResponse = new TransactionListResponse();
        transactionListResponse.setContent(content);
        transactionListResponse.setPageNo(listTransactionPage.getNumber());
        transactionListResponse.setPageSize(listTransactionPage.getSize());
        transactionListResponse.setTotalElements(listTransactionPage.getTotalElements());
        transactionListResponse.setTotalPages(listTransactionPage.getTotalPages());
        transactionListResponse.setLast(listTransactionPage.isLast());

        return transactionListResponse;
    }

    @Override
    public List<TransactionDto> getAllByUserId(Long userId) {
        return transactionRepository.findAllByUserId(userId).stream().map(transaction -> mapToDto(transaction)).collect(Collectors.toList());
    }

    private TransactionDto mapToDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setBank(transaction.getReceiver().getBank());
        transactionDto.setNote(transaction.getNote());
        transactionDto.setNumber(transaction.getNumber());
        transactionDto.setCreateAt(transaction.getCreateAt());
        transactionDto.setAccountNumberReceiver(transaction.getReceiver().getAccountNumber());
        transactionDto.setAccountNumberRemitter(transaction.getRemitter().getAccountNumber());
        transactionDto.setNameReceiver(transaction.getReceiver().getLastName() + " " + transaction.getReceiver().getFirstName());
        transactionDto.setNameRemitter(transaction.getRemitter().getLastName() + " " + transaction.getRemitter().getFirstName());

        return transactionDto;
    }

    private Transaction mapToEntity(TransactionForm transactionForm) {
        User receiver = userRepository.findByAccountNumber(transactionForm.getAccountNumber())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with account number " + transactionForm.getAccountNumber()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User remitter = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with account number " + name));

        Transaction transaction = new Transaction();

        transaction.setNote(transactionForm.getNote());
        transaction.setNumber(transactionForm.getNumber());
        transaction.setReceiver(receiver);
        transaction.setRemitter(remitter);
        return transaction;
    }
}
