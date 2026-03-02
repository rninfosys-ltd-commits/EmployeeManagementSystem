package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Student;

public interface StudentRepo extends JpaRepository<Student, Long>{

}
