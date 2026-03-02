package com.schoolapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.State;

@Repository
public interface StateRepo extends JpaRepository<State, Integer>{
	

}
