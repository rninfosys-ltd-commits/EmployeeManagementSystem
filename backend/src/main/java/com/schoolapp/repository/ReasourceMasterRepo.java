package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import com.schoolapp.entity.City;
import com.schoolapp.entity.ReasourceMaster;

@Repository
public interface ReasourceMasterRepo extends JpaRepository<ReasourceMaster, Integer> {

}
