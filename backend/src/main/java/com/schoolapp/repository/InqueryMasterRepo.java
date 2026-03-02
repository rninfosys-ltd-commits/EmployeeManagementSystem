package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.InqueryMaster;

@Repository
public interface InqueryMasterRepo extends JpaRepository<InqueryMaster, Integer>{

}
