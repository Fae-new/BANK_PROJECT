package com.test.bank.repositories;

import com.test.bank.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<MyUser,Long> {
    MyUser getByEmail(String email);
}
