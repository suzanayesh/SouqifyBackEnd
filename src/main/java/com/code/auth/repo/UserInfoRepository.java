package com.code.auth.repo;

import com.code.auth.entity.Role;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
  Optional<UserInfo> findByName(String name);
  List<UserInfo> findByRoleId(int roleId);
  List<UserInfo> findByRole_Id(int roleId);

//  Optional<UserProfile> findByUser(UserInfo user);

  Optional<UserInfo> findByEmail(String email);
  List<UserInfo> findByRole(Role role);
//  Optional<UserInfo> findById(int userId); // where 1 is an Integer
//  Optional<UserInfo> findByUsername(String username);
//  boolean existsByUsername(String username);
//  boolean existsByEmail(String email);
//  boolean existsByName(String name);
  List<UserInfo> findByNameContaining(String name);

  void deleteById(Long id);

  boolean existsById(Long id);

  Optional<UserInfo>  findById(Long id);

//  Optional<Object> findById(Long );
}
