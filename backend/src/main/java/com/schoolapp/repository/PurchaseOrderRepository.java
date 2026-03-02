package com.schoolapp.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.PurchaseOrder;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    List<PurchaseOrder> findByUserId(int userId);

    List<PurchaseOrder> findByPurchaseDateBetween(Date start, Date end);

    List<PurchaseOrder> findByIsActive(int isActive);
}
