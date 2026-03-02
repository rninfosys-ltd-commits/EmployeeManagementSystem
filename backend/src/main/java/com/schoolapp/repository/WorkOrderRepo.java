package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.WorkOrder;

@Repository
public interface WorkOrderRepo extends JpaRepository<WorkOrder, Integer> {

}
