package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Role;
import com.consulteer.shoppingstore.dtos.RoleDto;
import com.consulteer.shoppingstore.mapper.RoleMapper;
import com.consulteer.shoppingstore.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @InjectMocks
    private RoleServiceImpl underTest;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleMapper roleMapper;
    private static Role role;
    private static RoleDto roleDto;

    @BeforeEach
    void setUp() {
        role = new Role("ROLE_DUMMY");
        roleDto = new RoleDto("ROLE_DUMMY");
    }

    @Test
    void shouldAddRole() {
        //given
        when(roleMapper.convert(any())).thenReturn(role);
        when(roleRepository.save(any())).thenReturn(role);

        //when
        var result = underTest.addRole(roleDto);

        //then
        assertNotNull(result);
        assertEquals(true, result.getSuccess());
        assertEquals("Role added successfully", result.getMessage());

        verify(roleMapper, times(1)).convert(any());
        verify(roleRepository, times(1)).save(any());
        verifyNoMoreInteractions(roleMapper);
        verifyNoMoreInteractions(roleRepository);
    }
}