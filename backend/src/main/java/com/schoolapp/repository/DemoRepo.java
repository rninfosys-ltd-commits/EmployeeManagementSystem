package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.DemoEntity;

public interface DemoRepo extends JpaRepository<DemoEntity , Integer>{

}
