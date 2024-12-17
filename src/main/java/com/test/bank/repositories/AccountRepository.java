package com.test.bank.repositories;

import com.test.bank.entities.Account;
import com.test.bank.entities.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByAccountNumber(Long accountNumber);
    List<Account> findByAccountType(AccountType accountType);
}
