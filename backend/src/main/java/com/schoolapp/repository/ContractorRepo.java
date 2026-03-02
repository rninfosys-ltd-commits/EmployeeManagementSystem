package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Contractor;

public interface ContractorRepo extends JpaRepository<Contractor, Integer> {

}
