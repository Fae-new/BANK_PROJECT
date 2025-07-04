package com.test.bank.repositories;

import com.test.bank.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyUser,Integer> {
    Optional<MyUser> findByEmail(String email);
}
