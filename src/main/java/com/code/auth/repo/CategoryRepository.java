package com.code.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.code.auth.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // You can add more methods if needed
   // Optional<Category> findByName(String name);
}





//package com.code.auth.repo;
//
//
//import java.util.Optional;
//
//import com.code.auth.entity.Category;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//
//@Repository
//public interface CategoryRepository extends JpaRepository<Category, Long> {
//    Optional<Category> findByName(String name);
//}
