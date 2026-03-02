package com.schoolapp.repository;

import java.util.List;

//package com.codewithswati.crmemps.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.UserEntity;

//import com.crmemps.entity.UserEntity;

//import com.codewithswati.crmemps.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByRole(String role);

    boolean existsByEmail(String email);
    Optional<UserEntity> findByUsername(String username);  
}

