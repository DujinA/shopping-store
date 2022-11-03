package com.consulteer.shoppingstore.mapper;

import com.consulteer.shoppingstore.domain.Role;
import com.consulteer.shoppingstore.dtos.RoleDto;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public Role convert(RoleDto roleDto) {
        return new Role(roleDto.name());
    }
}
