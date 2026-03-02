package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.schoolapp.entity.SalesOrderMaster;

@Repository
public interface SalesOrderMasterRepo
		extends JpaRepository<SalesOrderMaster, Integer>, JpaSpecificationExecutor<SalesOrderMaster> {
}
