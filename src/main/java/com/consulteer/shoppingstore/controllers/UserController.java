package com.consulteer.shoppingstore.controllers;

import com.consulteer.shoppingstore.dtos.CreateUserDto;
import com.consulteer.shoppingstore.dtos.UserDto;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{user-id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("user-id") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserDto createUserDto) {
        return new ResponseEntity<>(userService.createUser(createUserDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @PutMapping("/{user-id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable("user-id") Long userId) {
        return ResponseEntity.ok(userService.updateUserInfo(userDto, userId));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @DeleteMapping("/{user-id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("user-id") Long userId) {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }
}
