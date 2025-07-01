package com.test.bank.services;

import com.test.bank.entities.MyUser;
import com.test.bank.entities.security.SecurityUser;
import com.test.bank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> myUserOptional = repository.findByEmail(username);
    return myUserOptional.map(SecurityUser::new).orElseThrow(()->new UsernameNotFoundException("Email " +username + "not found"));
    }
}
