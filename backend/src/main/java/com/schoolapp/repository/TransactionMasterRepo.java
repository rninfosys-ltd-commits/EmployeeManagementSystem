package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.TransactionMaster;
@Repository
public interface TransactionMasterRepo extends JpaRepository<TransactionMaster, Integer> {

}
