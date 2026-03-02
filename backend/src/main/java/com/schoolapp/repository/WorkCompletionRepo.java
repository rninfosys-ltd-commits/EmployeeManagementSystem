package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.WorkCompletion;

@Repository
public interface WorkCompletionRepo extends JpaRepository<WorkCompletion, Integer>{

}
