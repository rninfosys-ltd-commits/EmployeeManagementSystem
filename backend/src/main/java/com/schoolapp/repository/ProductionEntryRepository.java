package com.schoolapp.repository;

//import com.Crmemp.entity.ProductionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.ProductionEntry;

@Repository
public interface ProductionEntryRepository extends JpaRepository<ProductionEntry, Long> {
    boolean existsByBatchNo(String batchNo);

    java.util.List<ProductionEntry> findByCreatedDateBetween(java.util.Date start, java.util.Date end);

    java.util.List<ProductionEntry> findByBatchNo(String batchNo);
}
