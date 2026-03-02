package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.ProductionRecipe;
@Repository
public interface ProductionRecipeRepo extends JpaRepository<ProductionRecipe, Integer>{

}
