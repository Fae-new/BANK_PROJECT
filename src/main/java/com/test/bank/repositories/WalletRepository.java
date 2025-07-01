package com.test.bank.repositories;

import com.test.bank.entities.Wallet;
import com.test.bank.entities.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByWalletNumber(Long accountNumber);
    List<Wallet> findByWalletType(WalletType walletType);
}
