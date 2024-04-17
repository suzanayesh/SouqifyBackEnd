package com.code.auth.repo;

import com.code.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
//  Optional<UserInfo> findByName(String username);
Optional<Role> findByName(String roleName);

}
