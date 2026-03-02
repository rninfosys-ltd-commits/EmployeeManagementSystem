package com.schoolapp.repository;

import com.schoolapp.entity.MaterialMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialMasterRepository extends JpaRepository<MaterialMaster, Long> {

    List<MaterialMaster> findByIsActiveAndOrgIdOrderByDisplayOrderAsc(int isActive, int orgId);

    List<MaterialMaster> findByIsActiveOrderByDisplayOrderAsc(int isActive);
}
