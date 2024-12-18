package com.test.bank.services;

import com.test.bank.entities.MyUser;
import com.test.bank.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder encoder= new BCryptPasswordEncoder(10);
    private final JwtService jwtService;
    private final  MyUserDetailsService myUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;


    public UserService(MyUserDetailsService myUserDetailsService,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserRepository repository){
        this.repository=repository;
        this.myUserDetailsService=myUserDetailsService;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }
    public void addUser(MyUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
     repository.save(user);
    }

    public Optional<String> verify(MyUser user) {

        UserDetails studentDetails = myUserDetailsService.loadUserByUsername(user.getEmail());

        Authentication authentication= authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        String token = jwtService.generateToken(studentDetails);

        if( authentication.isAuthenticated()){
            return  Optional.of(token);
        }
        return Optional.empty();
    }
}
