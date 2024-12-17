package com.test.bank.controllers;

import com.test.bank.entities.Dtos.AccountRequest;
import com.test.bank.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {


    private final AccountService accountService;

public AccountController( AccountService accountService){
    this.accountService=accountService;

}


    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody AccountRequest accountRequest) {
        try {
            String message = accountService.createAccount(accountRequest.getUserId(),
                    accountRequest.getAccountName(),
                    accountRequest.getAccountType());
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create account: " + e.getMessage(), HttpStatus.BAD_REQUEST); // 400 for failure
        }
    }
}
