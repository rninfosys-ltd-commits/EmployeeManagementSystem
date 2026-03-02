package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.GRNEntry;

@Repository
public interface GRNEntryRepo extends JpaRepository<GRNEntry, Integer> {

}
