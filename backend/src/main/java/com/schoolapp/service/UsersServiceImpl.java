package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.schoolapp.config.JwtUtils;

import com.schoolapp.dao.LoginRequest;
import com.schoolapp.dao.LoginResponse;
import com.schoolapp.dao.RegisterRequest;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.repository.UserRepository;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserEntity getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setMobile(request.getMobile());
        user.setProfileImage(request.getProfileImage());
        // user.setParentId(1L); // Removed hardcoded default; handled by entity default
        // or specific logic

        userRepository.save(user);
    }

    // ================= LOGIN =================

    @Override
    public LoginResponse login(LoginRequest request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate real JWT token
        String token = jwtUtils.generateToken(user.getEmail());

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                token);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUserStatus(Long id, boolean active) {
        userRepository.findById(id).ifPresent(user -> {
            user.setActive(active ? 1 : 0);
            userRepository.save(user);
        });
    }

    @Override
    public UserEntity updateUser(Long id, UserEntity userDetails) {
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        existing.setUsername(userDetails.getUsername());
        existing.setEmail(userDetails.getEmail());
        existing.setRole(userDetails.getRole());
        existing.setMobile(userDetails.getMobile());
        existing.setProfileImage(userDetails.getProfileImage());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(existing);
    }
}
