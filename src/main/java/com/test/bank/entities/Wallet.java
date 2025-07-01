package com.test.bank.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int walletId;


    @Column(unique = true, nullable = false)
    private String walletName;


    @Column(nullable = false, unique = true)
    private Long walletNumber;


    @Column(nullable = false)
    private Long walletBalance;

    @OneToMany(mappedBy = "transactionCreatorWallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> debitTransactions;

    @OneToMany(mappedBy = "transactionRecipientWallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> creditTransactions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType walletType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser user;

}
