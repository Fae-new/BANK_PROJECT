package com.test.bank.controllers;

import com.test.bank.entities.Transaction;
import com.test.bank.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {


    private final AccountService accountService;

    public TransactionController(AccountService accountService ){

        this.accountService= accountService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long accountNumber, @RequestParam Long amount) {
        String message = accountService.deposit(accountNumber, amount);
        return ResponseEntity.ok(message);
    }


    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam Long accountNumber, @RequestParam Long amount) {
        String message = accountService.withdraw(accountNumber, amount);
        return ResponseEntity.ok(message);
    }
@GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@RequestParam Long userId){
        List<Transaction> transactions = accountService.getHistory(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);

}


}
