package com.consulteer.shoppingstore.services;

import com.consulteer.shoppingstore.dtos.UserDto;
import com.consulteer.shoppingstore.payloads.ApiResponse;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto addUser(UserDto userDto);

    UserDto updateUserInfo(UserDto userDto, Long userId);

    ApiResponse deleteUser(Long userId);
}
