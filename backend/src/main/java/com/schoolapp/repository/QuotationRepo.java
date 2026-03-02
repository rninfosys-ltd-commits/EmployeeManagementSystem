package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Quotation;



public interface QuotationRepo extends JpaRepository<Quotation, Integer>{

}
