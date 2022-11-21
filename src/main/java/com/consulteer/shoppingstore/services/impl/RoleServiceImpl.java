package com.consulteer.shoppingstore.services.impl;

import com.consulteer.shoppingstore.domain.Role;
import com.consulteer.shoppingstore.dtos.RoleDto;
import com.consulteer.shoppingstore.mapper.RoleMapper;
import com.consulteer.shoppingstore.payloads.ApiResponse;
import com.consulteer.shoppingstore.repositories.RoleRepository;
import com.consulteer.shoppingstore.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public ApiResponse addRole(RoleDto roleDto) {
        Role role = roleMapper.convert(roleDto);

        roleRepository.save(role);

        return new ApiResponse("Role added successfully", true);
    }
}
