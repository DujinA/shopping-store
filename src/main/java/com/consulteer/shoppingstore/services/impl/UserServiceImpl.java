package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Basket;
import com.consulteer.shoppingstore.domain.Role;
import com.consulteer.shoppingstore.domain.User;
import com.consulteer.shoppingstore.dtos.CreateUserDto;
import com.consulteer.shoppingstore.dtos.UserDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.UserMapper;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.repositories.RoleRepository;
import com.consulteer.shoppingstore.repositories.UserRepository;
import com.consulteer.shoppingstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
    @Transactional
    public UserDto createUser(CreateUserDto createUserDto) {
        User user = userMapper.convertCreatedUser(createUserDto);
        Role role = roleRepository.findByName(createUserDto.roleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role does not exist"));

        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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
    @Transactional
    public UserDto updateUserInfo(UserDto userDto, Long userId) {
        User updatedUser = findUserById(userId);

        updateBasicFields(userDto, updatedUser);
        userRepository.save(updatedUser);

        return userMapper.convert(updatedUser);
    }

    private static void updateBasicFields(UserDto userDto, User updatedUser) {
        updatedUser.setUsername(userDto.username());
        updatedUser.setFirstName(userDto.firstName());
        updatedUser.setLastName(userDto.lastName());
        updatedUser.setEmail(userDto.email());
    }

    @Override
    @Transactional
    public ApiResponse deleteUser(Long userId) {
        User user = findUserById(userId);
        userRepository.delete(user);

        return new ApiResponse("User deleted successfully", true);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
