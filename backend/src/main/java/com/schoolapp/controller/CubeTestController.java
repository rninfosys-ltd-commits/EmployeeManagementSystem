package com.schoolapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.dao.CubeTestDTO;
import com.schoolapp.entity.CubeTestEntity;
import com.schoolapp.service.CubeTestService;

@RestController
@RequestMapping("/api/cube-test")

public class CubeTestController {

    private final CubeTestService service;

    // ✅ ADD THIS CONSTRUCTOR
    public CubeTestController(CubeTestService service) {
        this.service = service;
    }

    @PostMapping
    public CubeTestEntity save(@RequestBody CubeTestDTO dto) {

        CubeTestEntity e = new CubeTestEntity();
        BeanUtils.copyProperties(dto, e);

        return service.save(e);
    }

    @GetMapping
    public List<CubeTestEntity> list() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    public CubeTestEntity update(@PathVariable Long id, @RequestBody CubeTestDTO dto) {

        CubeTestEntity e = new CubeTestEntity();
        BeanUtils.copyProperties(dto, e);

        return service.update(id, e);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/approve")
    public CubeTestEntity approve(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String role) {
        return service.approve(id, userId, role);
    }

    @PostMapping("/{id}/reject")
    public CubeTestEntity reject(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String role,
            @RequestBody Map<String, String> body) {

        return service.reject(id, body.get("reason"), userId, role);
    }

}
