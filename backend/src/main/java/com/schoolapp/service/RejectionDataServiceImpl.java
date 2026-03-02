package com.schoolapp.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.RejectionDataDTO;
import com.schoolapp.entity.RejectionDataEntity;
import com.schoolapp.repository.RejectionDataRepository;

@Service
public class RejectionDataServiceImpl implements RejectionDataService {

    @Autowired
    private RejectionDataRepository repo;

    // ================= CREATE =================
    @Override
    public Object create(RejectionDataDTO dto) {

        Long userId = dto.getUserId() != null ? dto.getUserId().longValue() : null;

        RejectionDataEntity entity = new RejectionDataEntity();
        BeanUtils.copyProperties(dto, entity);

        entity.setUserId(userId != null ? userId.intValue() : null);
        entity.setUpdatedBy(userId != null ? userId.intValue() : null);
        entity.setCreatedDate(new Date());
        entity.setIsActive(1);

        return repo.save(entity);
    }

    // ================= GET ALL =================
    @Override
    public Object getAll() {
        return repo.findAll();
    }

    // ================= GET BY ID =================
    @Override
    public Object getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ================= UPDATE =================
    @Override
    public Object update(Long id, RejectionDataDTO dto) {

        RejectionDataEntity entity = repo.findById(id).orElse(null);
        if (entity == null)
            return null;

        BeanUtils.copyProperties(dto, entity, "id", "createdDate", "userId");

        Long userId = dto.getUserId() != null ? dto.getUserId().longValue() : null;

        entity.setUpdatedBy(userId != null ? userId.intValue() : null);
        entity.setUpdatedDate(new Date());

        return repo.save(entity);
    }

    // ================= DELETE =================
    @Override
    public Object delete(Long id) {

        if (!repo.existsById(id))
            return null;

        repo.deleteById(id); // hard delete

        return "Deleted Successfully";
    }
}
