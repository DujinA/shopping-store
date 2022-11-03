package com.consulteer.shoppingstore.dtos;

public record CreateUserDto(String username,
                            String password,
                            String firstName,
                            String lastName,
                            String email,
                            String roleName) {
}
