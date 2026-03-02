package com.schoolapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.PurchaseCartItems;
import com.schoolapp.entity.PurchaseOrders;

@Repository
public interface PurchaseCartItemsRepository extends JpaRepository<PurchaseCartItems, Long> {

    List<PurchaseCartItems> findByPurchaseOrder(PurchaseOrders purchaseOrder);
}
