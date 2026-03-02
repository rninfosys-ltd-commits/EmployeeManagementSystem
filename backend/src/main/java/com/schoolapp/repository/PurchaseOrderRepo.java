package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

// import com.schoolapp.entity.InqueryDetailes;
import com.schoolapp.entity.PurchaseOrder;

public interface PurchaseOrderRepo extends JpaRepository<PurchaseOrder, Integer> {

}
