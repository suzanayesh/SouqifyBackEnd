package com.code.auth.service;

import com.code.auth.entity.Role;
import com.code.auth.repo.RoleRepository;
import com.code.auth.repo.UserInfoRepository;

public class RoleService {
  private RoleRepository roleRepository;

    public Role getRoleById(int roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
    }

}
