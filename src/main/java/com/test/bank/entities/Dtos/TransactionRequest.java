package com.test.bank.entities.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionRequest {

    private Long accountNumber;
    private Long amount;
    private Long receiverAccountNumber;
}
