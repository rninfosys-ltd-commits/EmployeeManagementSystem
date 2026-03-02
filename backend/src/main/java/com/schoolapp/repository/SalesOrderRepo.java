package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.SalesOrder;


public interface SalesOrderRepo extends JpaRepository<SalesOrder, Integer> {

}
