package com.schoolapp.service;

import org.springframework.stereotype.Service;

import com.schoolapp.dao.InquiryScheduleDto;
import com.schoolapp.entity.InquirySchedule;
import com.schoolapp.repository.InquiryScheduleRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InquiryScheduleServiceImpl implements InquiryScheduleService {

    private final InquiryScheduleRepository repository;

    public InquiryScheduleServiceImpl(InquiryScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public InquiryScheduleDto save(InquiryScheduleDto dto) {

        InquirySchedule entity = new InquirySchedule();

        entity.setInquiryId(dto.getInquiryId()); // 🔥 IMPORTANT FIX
        entity.setOrgId(dto.getOrgId());
        entity.setBranchId(dto.getBranchId());
        entity.setUserId(dto.getUserId());
        entity.setScheduleDate(dto.getScheduleDate());
        entity.setScheTime(dto.getScheTime());
        entity.setRemark(dto.getRemark());
        entity.setInqStatus(dto.getInqStatus());
        entity.setAssignTo(dto.getAssignTo());
        entity.setCreateDate(new Date());

        InquirySchedule saved = repository.save(entity);

        dto.setInqId(saved.getInqId()); // schedule id
        dto.setInquiryId(saved.getInquiryId()); // inquiry id

        return dto;
    }

    @Override
    public InquiryScheduleDto update(Long id, InquiryScheduleDto dto) {

        InquirySchedule entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        entity.setInquiryId(dto.getInquiryId()); // 🔥 IMPORTANT FIX
        entity.setScheduleDate(dto.getScheduleDate());
        entity.setScheTime(dto.getScheTime());
        entity.setRemark(dto.getRemark());
        entity.setInqStatus(dto.getInqStatus());
        entity.setAssignTo(dto.getAssignTo());
        entity.setUpdatedDate(new Date());
        entity.setUpdatedBy(dto.getUserId());

        repository.save(entity);
        return dto;
    }

    @Override
    public List<InquiryScheduleDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public InquiryScheduleDto getById(Long id) {
        InquirySchedule entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        return mapToDto(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private InquiryScheduleDto mapToDto(InquirySchedule entity) {

        InquiryScheduleDto dto = new InquiryScheduleDto();

        dto.setInqId(entity.getInqId()); // Schedule ID
        dto.setInquiryId(entity.getInquiryId()); // Inquiry ID
        dto.setOrgId(entity.getOrgId());
        dto.setBranchId(entity.getBranchId());
        dto.setUserId(entity.getUserId());
        dto.setScheduleDate(entity.getScheduleDate());
        dto.setScheTime(entity.getScheTime());
        dto.setRemark(entity.getRemark());
        dto.setInqStatus(entity.getInqStatus());
        dto.setAssignTo(entity.getAssignTo());

        return dto;
    }
}
