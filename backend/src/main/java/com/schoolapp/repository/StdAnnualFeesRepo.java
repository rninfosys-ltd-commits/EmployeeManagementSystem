package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.schoolapp.entity.StdAnnualFees;

@Repository
public interface StdAnnualFeesRepo extends JpaRepository<StdAnnualFees, Integer> {

}
