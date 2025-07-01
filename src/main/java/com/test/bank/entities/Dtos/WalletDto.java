package com.test.bank.entities.Dtos;


import com.test.bank.entities.WalletType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class WalletDto {
    private int walletId;
    private String walletName;
    private Long walletNumber;
    private Long walletBalance;
   private List<TransactionDto> creditTransactions;
   private List<TransactionDto> debitTransactions;
    private WalletType walletType;
}
