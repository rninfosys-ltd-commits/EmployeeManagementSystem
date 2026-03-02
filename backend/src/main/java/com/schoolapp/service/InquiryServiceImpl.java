package com.schoolapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.schoolapp.dao.InquiryDto;
import com.schoolapp.dao.InquiryImportRequest;
import com.schoolapp.entity.Inquiry;
import com.schoolapp.repository.InquiryRepository;

@Service
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;

    public InquiryServiceImpl(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    // ================= CREATE =================
    @Override
    public InquiryDto createInquiry(InquiryDto dto) {

        boolean exists = inquiryRepository
                .existsByLeadAccountIdAndProjectCodeAndUnitCode(
                        dto.getLeadAccountId(),
                        dto.getProjectCode(),
                        dto.getUnitCode());

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Inquiry already exists for this Lead + Project + Unit");
        }

        Inquiry inquiry = mapToEntity(dto);
        inquiry.setCreatedDate(new Date());

        return mapToDto(inquiryRepository.save(inquiry));
    }

    // ================= UPDATE =================
    @Override
    public InquiryDto updateInquiry(int id, InquiryDto dto) {

        boolean exists = inquiryRepository
                .existsByLeadAccountIdAndProjectCodeAndUnitCodeAndInqueryIdNot(
                        dto.getLeadAccountId(),
                        dto.getProjectCode(),
                        dto.getUnitCode(),
                        id);

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Another inquiry already exists with same Lead + Project + Unit");
        }

        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Inquiry not found"));

        inquiry.setInqStatusId(dto.getInqStatusId());
        inquiry.setInqueryDate(dto.getInqueryDate());
        inquiry.setLeadAccountId(dto.getLeadAccountId());
        inquiry.setProjectCode(dto.getProjectCode());
        inquiry.setUnitCode(dto.getUnitCode());
        inquiry.setRate(dto.getRate());
        inquiry.setQuantity(dto.getQuantity());
        inquiry.setAmount(dto.getAmount());
        inquiry.setTotal(dto.getTotal());
        inquiry.setParticulars(dto.getParticulars());
        inquiry.setIsActive(dto.getIsActive());
        inquiry.setUpdatedDate(new Date());

        return mapToDto(inquiryRepository.save(inquiry));
    }

    // ================= GET ALL =================
    @Override
    public List<InquiryDto> getAllInquiries() {
        return inquiryRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ================= GET BY ID =================
    @Override
    public InquiryDto getInquiryById(int inquiryId) {
        return mapToDto(
                inquiryRepository.findById(inquiryId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Inquiry not found")));
    }

    // ================= DELETE =================
    @Override
    public void deleteInquiry(int inquiryId) {
        inquiryRepository.deleteById(inquiryId);
    }

    // ================= MAPPERS =================
    private Inquiry mapToEntity(InquiryDto dto) {
        Inquiry i = new Inquiry();
        i.setInqStatusId(dto.getInqStatusId());
        i.setInqueryDate(dto.getInqueryDate());
        i.setLeadAccountId(dto.getLeadAccountId());
        i.setProjectCode(dto.getProjectCode());
        i.setUnitCode(dto.getUnitCode());
        i.setRate(dto.getRate());
        i.setQuantity(dto.getQuantity());
        i.setAmount(dto.getAmount());
        i.setTotal(dto.getTotal());
        i.setParticulars(dto.getParticulars());
        i.setOrgId(dto.getOrgId());
        i.setBranchId(dto.getBranchId());
        i.setUserId(dto.getUserId());
        i.setIsActive(dto.getIsActive());
        return i;
    }

    private InquiryDto mapToDto(Inquiry i) {
        InquiryDto dto = new InquiryDto();
        dto.setInqueryId(i.getInqueryId());
        dto.setInqStatusId(i.getInqStatusId());
        dto.setInqueryDate(i.getInqueryDate());
        dto.setLeadAccountId(i.getLeadAccountId());
        dto.setProjectCode(i.getProjectCode());
        dto.setUnitCode(i.getUnitCode());
        dto.setRate(i.getRate());
        dto.setQuantity(i.getQuantity());
        dto.setAmount(i.getAmount());
        dto.setTotal(i.getTotal());
        dto.setParticulars(i.getParticulars());
        dto.setOrgId(i.getOrgId());
        dto.setBranchId(i.getBranchId());
        dto.setUserId(i.getUserId());
        dto.setIsActive(i.getIsActive());
        return dto;
    }

    @Override
    public Map<String, Object> importInquiries(InquiryImportRequest request) {

        List<Map<String, Object>> results = new ArrayList<>();
        int saved = 0;
        int failed = 0;

        for (InquiryDto dto : request.getInquiries()) {
            try {
                createInquiry(dto); // reuse existing validation
                saved++;

                results.add(Map.of(
                        "leadAccountId", dto.getLeadAccountId(),
                        "projectCode", dto.getProjectCode(),
                        "unitCode", dto.getUnitCode(),
                        "status", "SUCCESS",
                        "error", ""));

            } catch (Exception ex) {
                failed++;

                String errorMessage = "Failed";

                // ✅ DUPLICATE INQUIRY CASE
                if (ex instanceof ResponseStatusException &&
                        ((ResponseStatusException) ex).getStatusCode() == HttpStatus.CONFLICT) {

                    errorMessage = "Record already present";
                }

                results.add(Map.of(
                        "leadAccountId", dto.getLeadAccountId(),
                        "projectCode", dto.getProjectCode(),
                        "unitCode", dto.getUnitCode(),
                        "status", "FAILED",
                        "error", errorMessage));
            }

        }

        return Map.of(
                "savedCount", saved,
                "errorCount", failed,
                "results", results);
    }

}
