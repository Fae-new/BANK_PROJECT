package com.test.bank.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Transaction {

    @Id
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "creator_account_id", nullable = false)
    private Wallet transactionCreatorWallet;

    @ManyToOne
    @JoinColumn(name = "recipient_account_id")
    private Wallet transactionRecipientWallet;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private Long amount;
    private LocalDateTime transactionDate;

}
