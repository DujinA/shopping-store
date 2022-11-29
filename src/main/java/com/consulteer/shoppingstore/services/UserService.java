package com.consulteer.shoppingstore.services;

import com.consulteer.shoppingstore.dtos.CreateUserDto;
import com.consulteer.shoppingstore.dtos.UpdateUserDto;
import com.consulteer.shoppingstore.dtos.UserDto;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(CreateUserDto createUserDto);

    UserDto updateUserInfo(UpdateUserDto updateUserDto, Long userId);

    ApiResponse deleteUser(Long userId);
}
