package com.test.bank.entities.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
}
