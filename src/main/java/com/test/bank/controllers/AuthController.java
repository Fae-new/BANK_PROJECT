package com.test.bank.controllers;

import com.test.bank.entities.WalletType;
import com.test.bank.entities.MyUser;
import com.test.bank.services.WalletService;
import com.test.bank.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final WalletService walletService;



    @PostMapping("/register")
    public ResponseEntity<String> register( @Valid @RequestBody MyUser user) {
        try {
            userService.addUser(user);
            walletService.createAccount(user.getId(),"default", WalletType.SAVINGS);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error registering user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login( @Valid @RequestBody MyUser user) {
        try {
            Optional<String> res = userService.verify(user);
            Map<String, String> response = new HashMap<>();
            res.ifPresent(s -> response.put("jwt", s));

            return res
                    .map(s -> new ResponseEntity<>(response, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred during login: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
