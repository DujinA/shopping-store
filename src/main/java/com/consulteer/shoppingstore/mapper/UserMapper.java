package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.domain.User;
import com.consulteer.shoppingstore.dtos.CreateUserDto;
import com.consulteer.shoppingstore.dtos.UserDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserMapper {
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
}
