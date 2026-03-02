package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Dashboard; 
public interface DashboardRepo extends JpaRepository<Dashboard, Integer>{

}
