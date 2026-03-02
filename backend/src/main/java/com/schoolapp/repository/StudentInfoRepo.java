package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.StudentInfo;
 
@Repository
public interface StudentInfoRepo extends JpaRepository<StudentInfo, Integer> {

}
