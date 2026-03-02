package com.schoolapp.repository;

import com.schoolapp.entity.ProductionMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionMaterialRepository extends JpaRepository<ProductionMaterial, Long> {

    List<ProductionMaterial> findByProductionEntryId(Long productionEntryId);

    void deleteByProductionEntryId(Long productionEntryId);
}
