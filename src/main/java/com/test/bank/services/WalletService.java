package com.test.bank.services;

import com.test.bank.entities.*;
import com.test.bank.entities.Dtos.WalletDto;
import com.test.bank.entities.Dtos.TransactionRequest;
import com.test.bank.exceptions.WalletNotFoundException;
import com.test.bank.repositories.TransactionRepository;
import com.test.bank.repositories.WalletRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public WalletDto convertWalletToDto(Wallet wallet){
        if(wallet ==null) return null;
        return new WalletDto(wallet.getWalletId(), wallet.getWalletName(), wallet.getWalletNumber(), wallet.getWalletBalance(),wallet.getCreditTransactions().stream().map(transactionService::CreateTransactionDto).toList(),wallet.getDebitTransactions().stream().map(transactionService::CreateTransactionDto).toList(), wallet.getWalletType());
    }

    public String deposit(TransactionRequest transactionReq) {
        if (transactionReq.getAmount() <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero.");
        }
        Optional<Wallet> accountOpt = walletRepository.findByWalletNumber(transactionReq.getAccountNumber());

        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Wallet not found");
        }

        Wallet wallet = accountOpt.get();
        wallet.setWalletBalance(wallet.getWalletBalance() + transactionReq.getAmount());



        Transaction transaction = new Transaction(UUID.randomUUID().toString(), wallet,null,TransactionType.CREDIT,transactionReq.getAmount(),LocalDateTime.now());
        wallet.getDebitTransactions().add(transaction);
        walletRepository.save(wallet);
        return "Deposit successful. New Balance: " + wallet.getWalletBalance();
    }


    public String withdraw(TransactionRequest transactionReq) {

        if (transactionReq.getAmount() <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero.");
        }
        Optional<Wallet> accountOpt = walletRepository.findByWalletNumber(transactionReq.getAccountNumber());

        if (accountOpt.isEmpty()) {
            throw new WalletNotFoundException("Wallet not found");
        }

        Wallet wallet = accountOpt.get();

        if (wallet.getWalletBalance() < transactionReq.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setWalletBalance(wallet.getWalletBalance() - transactionReq.getAmount());



        Transaction transaction = new Transaction(UUID.randomUUID().toString(), wallet,null,TransactionType.DEBIT,transactionReq.getAmount(),LocalDateTime.now());
        wallet.getDebitTransactions().add(transaction);
        walletRepository.save(wallet);

        return "Withdrawal successful. New Balance: " + wallet.getWalletBalance();
    }

    public String createAccount(int userId, String name, WalletType walletType) {
        Optional<MyUser> userOpt = userService.findUserById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found. Please register first.");
        }

        MyUser user = userOpt.get();


        Long accountNumber = Long.parseLong(System.currentTimeMillis() % 1000000000 + "" + new Random().nextInt(1000));


        Wallet wallet = new Wallet();
        wallet.setWalletName(name);

        wallet.setWalletNumber(accountNumber);
        wallet.setWalletBalance(0L);
        wallet.setUser(user);
        wallet.setWalletType(walletType);
        user.getWallets().add(wallet);
        userService.saveUser(user);

        return "Wallet created successfully. Wallet Number: " + accountNumber;
    }


    @Scheduled(cron = "0 0 1 1 * ?")
    public void applyMonthlyInterest() {
        List<Wallet> savingsWallets = walletRepository.findByWalletType(WalletType.SAVINGS);
        for (Wallet wallet : savingsWallets) {
            long newBalance = calculateInterest(wallet);
            wallet.setWalletBalance(newBalance);
            walletRepository.save(wallet);

        }
    }


    private long calculateInterest(Wallet wallet) {
        double interestRate = 0.02;
        long balance = wallet.getWalletBalance();
        double interestAmount = balance * interestRate;
        return (long) (balance + interestAmount);
    }

    public String transfer(TransactionRequest transactionRequest) {
        Optional<Wallet> senderWalletOptional = walletRepository.findByWalletNumber(transactionRequest.getAccountNumber());
        Optional<Wallet> receiverWalletOptional = walletRepository.findByWalletNumber(transactionRequest.getReceiverAccountNumber());

        if (senderWalletOptional.isEmpty() || receiverWalletOptional.isEmpty()) {
            throw new RuntimeException("Invalid sender or receiver account number");
        }

        Wallet sender = senderWalletOptional.get();
        Wallet receiver = receiverWalletOptional.get();

        if (sender.getWalletBalance() < transactionRequest.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }

        sender.setWalletBalance(sender.getWalletBalance() - transactionRequest.getAmount());
        receiver.setWalletBalance(receiver.getWalletBalance() + transactionRequest.getAmount());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setTransactionId(UUID.randomUUID().toString());
        creditTransaction.setTransactionCreatorWallet(sender);
        creditTransaction.setTransactionRecipientWallet(receiver);  // Ensure receiver is set
        creditTransaction.setType(TransactionType.CREDIT);
        creditTransaction.setAmount(transactionRequest.getAmount());
        creditTransaction.setTransactionDate(LocalDateTime.now());

        Transaction debitTransaction = new Transaction();
        debitTransaction.setTransactionId(UUID.randomUUID().toString());
        debitTransaction.setTransactionCreatorWallet(sender);
        debitTransaction.setTransactionRecipientWallet(receiver);
        debitTransaction.setType(TransactionType.DEBIT);
        debitTransaction.setAmount(transactionRequest.getAmount());
        debitTransaction.setTransactionDate(LocalDateTime.now());

        // Ensure transactions are properly added to each wallet's lists
        sender.getDebitTransactions().add(debitTransaction);
        receiver.getCreditTransactions().add(creditTransaction);



        walletRepository.save(sender);
        walletRepository.save(receiver);
        System.out.println(sender.getDebitTransactions().size());
        System.out.println(receiver.getCreditTransactions().size());

        return "Transaction successful";
    }



    public List<WalletDto> getWallets(int userId, String accountName) {
        Optional<MyUser> userOptional = userService.findUserById(userId);


        if(userOptional.isEmpty()) throw new RuntimeException("User not found with id: " + userId);
        userOptional.get().getWallets().forEach(wallet ->
                System.out.println("Wallet ID: " + wallet.getCreditTransactions().size() + ", Wallet Name: " + wallet.getWalletName())
        );

       // userOptional.get().getWallets().getFirst().stream().map(this::convertWalletToDto).toList().forEach(System.out::println);
        return userOptional.get().getWallets().stream().map(this::convertWalletToDto).filter((walletDto -> accountName == null || walletDto.getWalletName().equals(accountName) )).toList();


    }
}
