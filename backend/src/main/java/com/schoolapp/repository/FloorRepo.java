package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.Floor;

@Repository
	public interface FloorRepo extends JpaRepository<Floor, Integer>{

}
