package com.test.bank.services;

import com.test.bank.entities.Account;
import com.test.bank.entities.AccountType;
import com.test.bank.entities.MyUser;
import com.test.bank.entities.Transaction;
import com.test.bank.repositories.AccountRepository;
import com.test.bank.repositories.TransactionRepository;
import com.test.bank.repositories.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {


    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          UserRepository userRepository){
        this.accountRepository=accountRepository;
        this.transactionRepository=transactionRepository;
        this.userRepository=userRepository;

    }
    public String deposit(Long accountNumber, Long amount) {
        if (amount <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero.");
        }
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);

        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOpt.get();
        account.setAccountBalance(account.getAccountBalance() + amount);
        accountRepository.save(account);


        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        return "Deposit successful. New Balance: " + account.getAccountBalance();
    }


    public String withdraw(Long accountNumber, Long amount) {

        if (amount <= 0) {
            throw  new RuntimeException("Withdrawal amount must be greater than zero.");
        }
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);

        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOpt.get();

        if (account.getAccountBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setAccountBalance(account.getAccountBalance() - amount);
        accountRepository.save(account);


        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType("WITHDRAW");
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        return "Withdrawal successful. New Balance: " + account.getAccountBalance();
    }

    public String createAccount(Long userId, String accountName, AccountType accountType) {
        Optional<MyUser> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found. Please register first.");
        }

        MyUser user = userOpt.get();


        int accountNumber = new Random().nextInt(900000) + 100000;

        Account account = new Account();
        account.setAccountName(accountName);
        account.setAccountNumber(accountNumber);
        account.setAccountBalance(0L); // Initial balance
        account.setUser(user);
        account.setAccountType(accountType);
user.setAccount(account);
        accountRepository.save(account);

        return "Account created successfully. Account Number: " + accountNumber;
    }

    public List<Transaction> getHistory(Long userId) {
        Optional<MyUser> userOptional =userRepository.findById((userId));
        if(userOptional.isEmpty())  throw new RuntimeException("User not found. Please register first.");
List<Transaction> transactions= userOptional.get().getAccount().getTransactions();

        if (transactions == null || transactions.isEmpty()) {
            return new ArrayList<>();
        }
        return transactions;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void applyMonthlyInterest() {
        List<Account> savingsAccounts = accountRepository.findByAccountType(AccountType.SAVINGS);
        for (Account account : savingsAccounts) {
                long newBalance = calculateInterest(account);
                account.setAccountBalance(newBalance);
                accountRepository.save(account);

        }
    }


    private long calculateInterest(Account account) {
        double interestRate = 0.02;
        long balance = account.getAccountBalance();
        double interestAmount = balance * interestRate;
        return (long) (balance + interestAmount);
    }
}
