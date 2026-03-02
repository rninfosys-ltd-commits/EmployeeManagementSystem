package com.schoolapp.service;

import java.util.List;
//import com.Crmemp.entity.CubeTestEntity;

import com.schoolapp.entity.CubeTestEntity;

public interface CubeTestService {

    CubeTestEntity save(CubeTestEntity cubeTest);

    CubeTestEntity update(Long id, CubeTestEntity cubeTest);

    List<CubeTestEntity> findAll();

    CubeTestEntity findById(Long id);

    void delete(Long id);

    CubeTestEntity approve(Long id, Long userId, String role);

    CubeTestEntity reject(Long id, String reason, Long userId, String role);

}
