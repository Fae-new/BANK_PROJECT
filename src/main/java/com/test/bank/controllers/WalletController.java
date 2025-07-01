package com.test.bank.controllers;


import com.test.bank.entities.Dtos.WalletDto;
import com.test.bank.entities.Dtos.CreateWalletRequestDto;
import com.test.bank.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<String> createWallet(@Valid @RequestBody CreateWalletRequestDto createWalletRequestDto) {
        try {
            String message = walletService.createAccount(createWalletRequestDto.getUserId(),
                    createWalletRequestDto.getWalletName(),
                    createWalletRequestDto.getWalletType());
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create account: " + e.getMessage(), HttpStatus.BAD_REQUEST); // 400 for failure
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WalletDto>> getAccounts(@PathVariable String userId, @RequestParam(required = false) String accountName){

if(userId==null)throw new RuntimeException("User id is absent");
List<WalletDto> accounts = walletService.getWallets( Integer.parseInt( userId),accountName);
        return new ResponseEntity<>(accounts,HttpStatus.OK);
    }

}
