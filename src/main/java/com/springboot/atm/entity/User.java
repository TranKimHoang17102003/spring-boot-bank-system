package com.springboot.atm.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username", name = "Username exists"),
        @UniqueConstraint(columnNames = "email", name = "Email exists"),
        @UniqueConstraint(columnNames = "phone_number", name = "Phone number exists")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "pin", nullable = false)
    private String pin;
    @Column(name = "account_number", nullable = false)
    private String accountNumber;
    @Column(name = "bank", nullable = false)
    private String bank;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;
    @Transient
    private Long age;
    @Column(name = "create_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createAt;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "money_id", referencedColumnName = "id")
    @JsonManagedReference
    private Money money;

    @OneToMany(mappedBy = "remitter", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Transaction> transactionRemitter;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Transaction> transactionReceiver;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Notify> notify;

    public Long getAge() {
        return (long) Period.between(this.birthday, LocalDate.now()).getYears();
    }
}
