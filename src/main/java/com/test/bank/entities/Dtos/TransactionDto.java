package com.test.bank.entities.Dtos;

import com.test.bank.entities.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class TransactionDto {
    private String transactionId;
    private String transactionCreatorName;
    private String transactionCreatorWalletName;
    private Long transactionCreatorWalletNumber;
    private String transactionRecipientAccountName;
    private String transactionRecipientWalletName;
    private Long transactionRecipientWalletNumber;
    private TransactionType type;
    private Long amount;
    private LocalDateTime transactionDate;
}
