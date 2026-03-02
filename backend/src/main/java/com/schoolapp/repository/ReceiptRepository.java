package com.schoolapp.repository;

//import com.employeemanagement.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Receipt;

//import com.Crmemp.entity.Receipt;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByCustomerId(Long customerId);
    List<Receipt> findByPartyId(Long partyId);
}
