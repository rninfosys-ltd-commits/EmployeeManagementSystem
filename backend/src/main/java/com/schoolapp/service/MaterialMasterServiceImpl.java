package com.schoolapp.service;

import com.schoolapp.entity.MaterialMaster;
import com.schoolapp.repository.MaterialMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialMasterServiceImpl implements MaterialMasterService {

    private final MaterialMasterRepository repository;

    public MaterialMasterServiceImpl(MaterialMasterRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MaterialMaster> getAllActive() {
        return repository.findByIsActiveOrderByDisplayOrderAsc(1);
    }

    @Override
    public List<MaterialMaster> getAllActiveByOrg(int orgId) {
        return repository.findByIsActiveAndOrgIdOrderByDisplayOrderAsc(1, orgId);
    }

    @Override
    public MaterialMaster save(MaterialMaster material) {
        return repository.save(material);
    }

    @Override
    public MaterialMaster update(Long id, MaterialMaster material) {
        MaterialMaster existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        existing.setMaterialName(material.getMaterialName());
        existing.setUnit(material.getUnit());
        existing.setDisplayOrder(material.getDisplayOrder());
        existing.setOrgId(material.getOrgId());
        existing.setBranchId(material.getBranchId());

        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        MaterialMaster existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        existing.setIsActive(0); // soft delete
        repository.save(existing);
    }
}
