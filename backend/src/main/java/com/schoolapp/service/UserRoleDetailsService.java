package com.schoolapp.service;

import com.schoolapp.entity.UserRoleDetails;
import com.schoolapp.repository.UserRoleDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserRoleDetailsService {

    @Autowired
    private UserRoleDetailsRepository repository;

    public List<UserRoleDetails> getAll() {
        return repository.findAll();
    }

    public UserRoleDetails save(UserRoleDetails details) {
        return repository.save(details);
    }

    public UserRoleDetails getByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
