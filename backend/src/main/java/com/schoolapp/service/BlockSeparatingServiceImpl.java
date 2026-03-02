package com.schoolapp.service;

import org.springframework.stereotype.Service;

import com.schoolapp.dao.BlockSeparatingDTO;
import com.schoolapp.entity.BlockSeparating;
import com.schoolapp.repository.BlockSeparatingRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlockSeparatingServiceImpl implements BlockSeparatingService {

    private final BlockSeparatingRepository repository;

    public BlockSeparatingServiceImpl(BlockSeparatingRepository repository) {
        this.repository = repository;
    }

    @Override
    public BlockSeparatingDTO create(BlockSeparatingDTO dto) {

        BlockSeparating e = new BlockSeparating();
        e.setShift(dto.getShift());
        e.setBatchNumber(dto.getBatchNumber());
        e.setCastingDate(dto.getCastingDate());
        e.setBlockSize(dto.getBlockSize());
        e.setTime(dto.getTime());
        e.setRemark(dto.getRemark());

        e.setUserId(dto.getUserId());
        e.setBranchId(dto.getBranchId());
        e.setOrgId(dto.getOrgId());
        e.setReportDate(dto.getReportDate());
        e.setCreatedDate(new Date());
        e.setUpdatedDate(new Date());
        e.setUpdatedBy(dto.getUserId());
        e.setIsActive(1);

        repository.save(e);
        dto.setId(e.getId());
        return dto;
    }

    @Override
    public BlockSeparatingDTO update(Long id, BlockSeparatingDTO dto) {

        BlockSeparating e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        e.setBatchNumber(dto.getBatchNumber());
        e.setCastingDate(dto.getCastingDate());
        e.setBlockSize(dto.getBlockSize());
        e.setTime(dto.getTime());
        e.setRemark(dto.getRemark());
        e.setShift(dto.getShift()); // ✅ ALLOW SHIFT UPDATE
        dto.setShift(dto.getShift());

        e.setUpdatedBy(dto.getUserId());
        e.setUpdatedDate(new Date());

        repository.save(e);
        dto.setId(id);
        return dto;
    }

    @Override
    public List<BlockSeparatingDTO> getAll() {
        return repository.findByIsActive(1)
                .stream()
                .map(e -> {
                    BlockSeparatingDTO dto = new BlockSeparatingDTO();
                    dto.setId(e.getId());
                    dto.setBatchNumber(e.getBatchNumber());
                    dto.setCastingDate(e.getCastingDate());
                    dto.setBlockSize(e.getBlockSize());
                    dto.setTime(e.getTime());
                    dto.setRemark(e.getRemark());
                    dto.setShift(e.getShift());
                    dto.setReportDate(e.getReportDate()); // ✅ ADD THIS

                    dto.setUserId(e.getUserId());
                    dto.setBranchId(e.getBranchId());
                    dto.setOrgId(e.getOrgId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BlockSeparatingDTO getById(Long id) {
        BlockSeparating e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        BlockSeparatingDTO dto = new BlockSeparatingDTO();
        dto.setId(e.getId());
        dto.setBatchNumber(e.getBatchNumber());
        dto.setCastingDate(e.getCastingDate());
        dto.setBlockSize(e.getBlockSize());
        dto.setTime(e.getTime());
        dto.setShift(e.getShift());

        dto.setRemark(e.getRemark());
        dto.setUserId(e.getUserId());
        dto.setBranchId(e.getBranchId());
        dto.setOrgId(e.getOrgId());
        return dto;
    }

    @Override
    public void delete(Long id) {
        BlockSeparating e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        e.setIsActive(0);
        e.setUpdatedDate(new Date());
        repository.save(e);
    }
}
