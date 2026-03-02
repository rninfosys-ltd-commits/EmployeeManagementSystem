package com.schoolapp.repository;

//import com.employeemanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Products;

//import com.Crmemp.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Products, Long> {

    List<Products> findAllByNameContaining(String name);

    List<Products> findByParentId(Long parentId);

    List<Products> findByCreatedBy_Id(Long userId);

    List<Products> findByParentIdOrCreatedBy_Id(Long parentId, Long userId);
    
}
