package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.QuotationMaster;



public interface QuotationMasterRepo extends JpaRepository<QuotationMaster, Integer>{

}
