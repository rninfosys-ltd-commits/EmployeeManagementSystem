package com.schoolapp.controller;

import com.schoolapp.entity.Route;
import com.schoolapp.service.RouteMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RoutesController {

    @Autowired
    private RouteMasterService rootService;

    @GetMapping
    public List<Route> getAll() {
        return rootService.getAll();
    }

    @PostMapping
    public Route save(@RequestBody Route root) {
        return rootService.save(root);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getById(@PathVariable Long id) {
        return rootService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rootService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
