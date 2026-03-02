package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.Category;
 

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
