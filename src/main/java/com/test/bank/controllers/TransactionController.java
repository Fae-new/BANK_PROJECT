package com.test.bank.controllers;

import com.test.bank.entities.Dtos.TransactionDto;
import com.test.bank.entities.Dtos.TransactionRequest;
import com.test.bank.services.WalletService;
import com.test.bank.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {


    private final WalletService walletService;
    private final TransactionService transactionService;


    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest transactionRequest) {
        String message = walletService.deposit(transactionRequest);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransactionRequest transactionRequest) {
        String message = walletService.transfer(transactionRequest);
        return ResponseEntity.ok(message);
    }


    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionRequest transactionRequest) {
        String message = walletService.withdraw(transactionRequest);
        return ResponseEntity.ok(message);
    }
@GetMapping("/history")
    public ResponseEntity<List<TransactionDto>> getTransactionHistory(@RequestParam int userId){
        List<TransactionDto> transactions = transactionService.getHistory(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);

}


}
