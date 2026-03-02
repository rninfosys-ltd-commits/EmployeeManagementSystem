package com.schoolapp.repository;

//import com.yourapp.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.State;

//import com.Crmemp.entity.State;

//import com.employeemanagement.entity.State;

public interface StateRepository extends JpaRepository<State, Long> {
    boolean existsByName(String name);
}
