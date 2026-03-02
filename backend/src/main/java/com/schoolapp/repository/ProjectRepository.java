package com.schoolapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Project;
//import com.Crmemp.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    boolean existsByProjectNameIgnoreCase(String projectName);

    Optional<Project> findByProjectNameIgnoreCase(String projectName);
}
