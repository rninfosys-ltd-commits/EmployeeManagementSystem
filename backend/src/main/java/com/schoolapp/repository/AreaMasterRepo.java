package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.AreaMaster;

@Repository
public interface AreaMasterRepo extends JpaRepository<AreaMaster, Integer>{



}
