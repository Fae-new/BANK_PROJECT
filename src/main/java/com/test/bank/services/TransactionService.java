package com.test.bank.services;

import com.test.bank.entities.Dtos.TransactionDto;
import com.test.bank.entities.MyUser;
import com.test.bank.entities.Transaction;
import com.test.bank.repositories.TransactionRepository;
import com.test.bank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
private final TransactionRepository transactionRepository;
    public TransactionDto CreateTransactionDto(Transaction transaction){

        return new TransactionDto(
                transaction.getTransactionId(),
                transaction.getTransactionCreatorWallet().getUser().getFullName(),
                transaction.getTransactionCreatorWallet().getWalletName(),
                transaction.getTransactionCreatorWallet().getWalletNumber(),
              transaction.getTransactionRecipientWallet()==null?null: transaction.getTransactionRecipientWallet().getUser().getFullName(),
                transaction.getTransactionRecipientWallet()==null?null:transaction.getTransactionRecipientWallet().getWalletName(),
                transaction.getTransactionRecipientWallet()==null?null: transaction.getTransactionRecipientWallet().getWalletNumber(),
                transaction.getType(),transaction.getAmount(),
                transaction.getTransactionDate());
    }
    public List<TransactionDto> getHistory(int userId) {

        Optional<MyUser> userOptional = userRepository.findById((userId));
        if (userOptional.isEmpty()) throw new RuntimeException("User not found. Please register first.");

        List<TransactionDto> transactions = userOptional.get().getWallets().stream().flatMap((account) -> account.getDebitTransactions().stream().map(this::CreateTransactionDto)).toList();

        if (transactions.isEmpty()) {
            return new ArrayList<>();
        }
        return transactions;
    }

    public void saveMultipleTransactions(List<Transaction> transactions){
        transactionRepository.saveAll(transactions);
    }
}
