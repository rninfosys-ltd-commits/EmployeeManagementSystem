package com.schoolapp.controller;

import com.schoolapp.entity.UserRoleDetails;
import com.schoolapp.service.UserRoleDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user-role-details")
public class UserRoleDetailsController {

    @Autowired
    private UserRoleDetailsService service;

    @GetMapping
    public List<UserRoleDetails> getAll() {
        return service.getAll();
    }

    @PostMapping
    public UserRoleDetails save(@RequestBody UserRoleDetails details) {
        return service.save(details);
    }

    @GetMapping("/username/{username}")
    public UserRoleDetails getByUsername(@PathVariable String username) {
        return service.getByUsername(username);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
