package com.springboot.atm.payload.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.atm.entity.Money;
import com.springboot.atm.entity.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String accountNumber;
    private String bank;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Long age;
    private LocalDateTime createAt;
    private Money money;
    private Set<String> roles = new HashSet<>();
}
