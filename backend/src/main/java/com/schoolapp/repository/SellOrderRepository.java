package com.schoolapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.schoolapp.entity.SellOrder;

@Repository
public interface SellOrderRepository extends JpaRepository<SellOrder, Long>, JpaSpecificationExecutor<SellOrder> {
    Optional<SellOrder> findById(Long id);
}
