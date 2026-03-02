package com.schoolapp.service;

import java.util.List;

import com.schoolapp.dao.LoginRequest;
import com.schoolapp.dao.LoginResponse;
import com.schoolapp.dao.RegisterRequest;
import com.schoolapp.entity.UserEntity;

public interface UsersService {
    UserEntity getUser(Long id);

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    List<UserEntity> getAllUsers();

    void deleteUser(Long id);

    void updateUserStatus(Long id, boolean active);

    UserEntity updateUser(Long id, UserEntity userDetails);
}
