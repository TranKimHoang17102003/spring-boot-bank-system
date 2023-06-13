package com.springboot.atm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "create_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createAt;
    @Column(name = "number", nullable = false)
    private Double number;
    @ManyToOne
    @JoinColumn(name = "remitter_id", nullable = false, referencedColumnName = "id")
    private User remitter;
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false, referencedColumnName = "id")
    private User receiver;
    @Column(name = "note", nullable = false)
    private String note;
}
