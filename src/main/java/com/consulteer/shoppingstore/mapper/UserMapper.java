package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.domain.User;
import com.consulteer.shoppingstore.dtos.CreateUserDto;
import com.consulteer.shoppingstore.dtos.UpdateUserDto;
import com.consulteer.shoppingstore.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserDto convert(User user) {
        return new UserDto(user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail());
    }

    public User convert(UserDto userDto) {
        return new User(userDto.username(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.email());
    }

    public User convertCreatedUser(CreateUserDto createUserDto) {
        return new User(createUserDto.username(),
                createUserDto.password(),
                createUserDto.firstName(),
                createUserDto.lastName(),
                createUserDto.email());
    }

    public void updateBasicFields(UpdateUserDto updateUserDto, User updatedUser) {
        if (updateUserDto.username() != null) {
            updatedUser.setUsername(updateUserDto.username());
        }
        if (updateUserDto.password() != null) {
            updatedUser.setPassword(passwordEncoder.encode(updateUserDto.password()));
        }
        if (updateUserDto.email() != null) {
            updatedUser.setEmail(updateUserDto.email());
        }
    }
}
