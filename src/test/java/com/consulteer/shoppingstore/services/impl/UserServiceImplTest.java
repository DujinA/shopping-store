package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Role;
import com.consulteer.shoppingstore.domain.User;
import com.consulteer.shoppingstore.dtos.CreateUserDto;
import com.consulteer.shoppingstore.dtos.UpdateUserDto;
import com.consulteer.shoppingstore.dtos.UserDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.mapper.UserMapper;
import com.consulteer.shoppingstore.repositories.RoleRepository;
import com.consulteer.shoppingstore.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl underTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    private static User user;
    private static UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User("marko123",
                "12345",
                "Marko",
                "Markovic",
                "marko@gmail.com");
        userDto = new UserDto("marko123",
                "Marko",
                "Markovic",
                "marko@gmail.com");
    }

    @Test
    void shouldGetAllUsers() {
        //given
        var userB = new User("jovan123",
                "Jovan",
                "Jovanovic",
                "jovan@gmail.com");
        var userBDto = new UserDto("jovan123",
                "Jovan",
                "Jovanovic",
                "jovan@gmail.com");

        when(userRepository.findAll()).thenReturn(List.of(user, userB));
        when(userMapper.convert(user)).thenReturn(userDto);
        when(userMapper.convert(userB)).thenReturn(userBDto);

        //when
        var result = underTest.getAllUsers();

        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("marko123", result.get(0).username());
        assertEquals("jovan123", result.get(1).username());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).convert(any(User.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    void shouldGetUserById() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.convert(any(User.class))).thenReturn(userDto);

        //when
        var result = underTest.getUserById(1L);

        //then
        assertNotNull(result);
        assertEquals("marko123", result.username());
        assertEquals("Marko", result.firstName());
        assertEquals("Markovic", result.lastName());
        assertEquals("marko@gmail.com", result.email());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userMapper, times(1)).convert(any(User.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserNotFoundWhileGetUserById() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getUserById(1L));

        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldCreateUser() {
        //given
        var createUserDto = new CreateUserDto("marko123",
                "12345",
                "Marko",
                "Markovic",
                "marko@gmail.com",
                "ROLE_DUMMY");
        var role = new Role("ROLE_DUMMY");

        when(userMapper.convertCreatedUser(any())).thenReturn(user);
        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any())).thenReturn("");
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.convert(any(User.class))).thenReturn(userDto);

        //when
        var result = underTest.createUser(createUserDto);

        //then
        assertNotNull(result);
        assertEquals("marko123", result.username());
        assertEquals("Marko", result.firstName());
        assertEquals("Markovic", result.lastName());
        assertEquals("marko@gmail.com", result.email());

        verify(userMapper, times(1)).convertCreatedUser(any());
        verify(roleRepository, times(1)).findByName(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).convert(any(User.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenRoleNotFoundWhileCreateUser() {
        //given
        var createUserDto = new CreateUserDto("marko123",
                "12345",
                "Marko",
                "Markovic",
                "marko@gmail.com",
                "ROLE_DUMMY");
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.createUser(createUserDto));

        verify(roleRepository, times(1)).findByName(any());
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void shouldUpdateUserInfo() {
        //given
        var updateUserDto = new UpdateUserDto("markomarkovic123",
                "password",
                "markomarkovic@gmail.com");
        var userDto = new UserDto("markomarkovic123",
                "Marko",
                "Markovic",
                "markomarkovic@gmail.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.convert(any(User.class))).thenReturn(userDto);

        //when
        var result = underTest.updateUserInfo(updateUserDto, 1L);

        //then
        assertNotNull(result);
        assertEquals("markomarkovic123", result.username());
        assertEquals("markomarkovic@gmail.com", result.email());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).convert(any(User.class));
        verify(userMapper, times(1)).updateBasicFields(any(), any());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserNotFoundWhileUpdateUserInfo() {
        //given
        var updateUserDto = new UpdateUserDto("markomarkovic123",
                "password",
                "markomarkovic@gmail.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateUserInfo(updateUserDto, 1L));

        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldDeleteUser() {
        //given - when
        var result = underTest.deleteUser(1L);

        //then
        assertEquals(true, result.getSuccess());
        assertEquals("User deleted successfully", result.getMessage());
    }

    @Test
    void shouldLoadUserByUsername() {
        //given
        var role = new Role("ROLE_DUMMY");
        user.setRole(role);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        //when
        var result = underTest.loadUserByUsername("ROLE_DUMMY");

        //then
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        var authority = Objects.requireNonNull(result.getAuthorities()
                        .stream()
                        .findAny()
                        .orElse(null))
                .getAuthority();
        assertEquals(role.getName(), authority);

        verify(userRepository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFoundWhileLoadUserByUsername() {
        //given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        //when - then
        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername("ROLE_DUMMY"));

        verify(userRepository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
    }
}