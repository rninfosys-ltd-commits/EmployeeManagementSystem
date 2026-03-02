package com.schoolapp.service;

import com.schoolapp.entity.Route;
import com.schoolapp.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RouteMasterService {

    @Autowired
    private RouteRepository rootRepository;

    public List<Route> getAll() {
        return rootRepository.findAll();
    }

    public Route save(Route root) {
        return rootRepository.save(root);
    }

    public Optional<Route> getById(Long id) {
        return rootRepository.findById(id);
    }

    public void delete(Long id) {
        rootRepository.deleteById(id);
    }
}
