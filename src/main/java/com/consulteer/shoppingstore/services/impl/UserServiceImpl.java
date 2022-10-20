package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Basket;
import com.consulteer.shoppingstore.domain.User;
import com.consulteer.shoppingstore.dtos.UserDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.UserMapper;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.repositories.UserRepository;
import com.consulteer.shoppingstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = findUserById(userId);

        return userMapper.convert(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with " + userId + " id"));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.convert(userDto);

        createBasketForUser(user);
        userRepository.save(user);

        return userMapper.convert(user);
    }

    private static void createBasketForUser(User user) {
        Basket basket = new Basket();
        basket.setUser(user);
        basket.setTotalPrice(0.00);
        basket.setTotalItemsCount(0);
        user.setBasket(basket);
    }

    @Override
    public UserDto updateUserInfo(UserDto userDto, Long userId) {
        User updatedUser = findUserById(userId);

        updateBasicFields(userDto, updatedUser);
        userRepository.save(updatedUser);

        return userMapper.convert(updatedUser);
    }

    private static void updateBasicFields(UserDto userDto, User updatedUser) {
        updatedUser.setUsername(userDto.getUsername());
        updatedUser.setFirstName(userDto.getFirstName());
        updatedUser.setLastName(userDto.getLastName());
        updatedUser.setEmail(userDto.getEmail());
    }

    @Override
    public ApiResponse deleteUser(Long userId) {
        User user = findUserById(userId);
        userRepository.delete(user);

        return new ApiResponse("User deleted successfully", true);
    }
}
