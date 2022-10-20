package com.consulteer.shoppingstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
}
