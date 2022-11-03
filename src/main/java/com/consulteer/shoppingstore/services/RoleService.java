package com.consulteer.shoppingstore.services;

import com.consulteer.shoppingstore.dtos.RoleDto;
import com.consulteer.shoppingstore.payloads.ApiResponse;

public interface RoleService {
    ApiResponse addRole(RoleDto roleDto);
}
