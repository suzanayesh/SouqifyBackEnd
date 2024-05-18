package com.code.auth.repo;

import com.code.auth.entity.Role;
import com.code.auth.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
  Optional<UserInfo> findByName(String name);

  Optional<UserInfo> findByEmail(String email);
  List<UserInfo> findByRole(Role role);
//  Optional<UserInfo> findById(int userId); // where 1 is an Integer

//  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  boolean existsByName(String name);
  List<UserInfo> findByNameContaining(String name);

  void deleteById(Long id);

  boolean existsById(Long id);

  Optional<UserInfo>  findById(Long id);

//  Optional<Object> findById(Long );
}
