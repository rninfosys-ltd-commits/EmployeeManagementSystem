package com.schoolapp.service;

import org.springframework.stereotype.Service;

import com.schoolapp.dao.InquiryLeadCreateDto;
import com.schoolapp.dao.LeadDto;
import com.schoolapp.entity.Lead;
import com.schoolapp.repository.LeadRepository;
import com.schoolapp.repository.StateRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final StateRepository stateRepository;

    public LeadServiceImpl(
            LeadRepository leadRepository,
            StateRepository stateRepository) {
        this.leadRepository = leadRepository;
        this.stateRepository = stateRepository;
    }

    // ================= CREATE =================
    @Override
    public LeadDto createLead(LeadDto dto) {

        // ✅ CUSTOMER NAME VALIDATION (ADD THIS)
        if (dto.getCustomerName() == null || dto.getCustomerName().trim().isEmpty()) {
            throw new RuntimeException("Customer name is required");
        }

        // 🔒 PAN UNIQUENESS CHECK
        if (dto.getPanNo() != null && !dto.getPanNo().isEmpty()) {
            boolean exists = leadRepository.existsByPanNo(dto.getPanNo());
            if (exists) {
                throw new RuntimeException("PAN number already exists");
            }
        }

        Lead lead = new Lead();

        lead.setDate(dto.getDate());
        lead.setCustomerName(dto.getCustomerName());
        lead.setContactNo(dto.getContactNo());
        lead.setPanNo(dto.getPanNo());
        lead.setGstNo(dto.getGstNo());
        lead.setEmail(dto.getEmail());
        lead.setWebsite(dto.getWebsite());
        lead.setPhone(dto.getPhone());
        lead.setFax(dto.getFax());
        lead.setInvoiceAddress(dto.getInvoiceAddress());

        lead.setIncome(dto.getIncome());
        lead.setIncomeSource(dto.getIncomeSource());
        lead.setOtherIncome(dto.getOtherIncome());
        lead.setOtherIncomeSource(dto.getOtherIncomeSource());
        lead.setBudget(dto.getBudget());
        lead.setNotes(dto.getNotes());
        lead.setArea(dto.getArea());

        lead.setStateId(dto.getStateId());
        lead.setDistId(dto.getDistId());
        lead.setCityId(dto.getCityId());

        lead.setUserId(dto.getUserId());
        lead.setBranchId(dto.getBranchId());
        lead.setOrgId(dto.getOrgId());

        lead.setIsActive(dto.getIsActive());
        lead.setCreatedDate(new Date());

        Lead saved = leadRepository.save(lead);
        dto.setLeadId(saved.getLeadId());

        return dto;
    }

    // ================= GET ALL =================
    @Override
    public List<LeadDto> getAllLeads() {
        return leadRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ================= GET BY ID =================
    @Override
    public LeadDto getLeadById(int leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));
        return mapToDto(lead);
    }

    // ================= UPDATE =================
    @Override
    public LeadDto updateLead(int leadId, LeadDto dto) {

        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        lead.setCustomerName(dto.getCustomerName());
        lead.setContactNo(dto.getContactNo());
        lead.setEmail(dto.getEmail());
        lead.setBudget(dto.getBudget());
        lead.setStateId(dto.getStateId());
        lead.setCityId(dto.getCityId());
        lead.setIsActive(dto.getIsActive());
        lead.setUpdatedDate(new Date());

        Lead updated = leadRepository.save(lead);
        return mapToDto(updated);
    }

    // ================= DELETE =================
    @Override
    public void deleteLead(int leadId) {
        leadRepository.deleteById(leadId);
    }

    // ================= MAPPER =================
    // ================= MAPPER =================
    private LeadDto mapToDto(Lead lead) {

        LeadDto dto = new LeadDto();

        dto.setLeadId(lead.getLeadId());
        dto.setDate(lead.getDate());

        dto.setCustomerName(lead.getCustomerName());

        dto.setContactNo(lead.getContactNo());
        dto.setPanNo(lead.getPanNo());
        dto.setGstNo(lead.getGstNo());
        dto.setEmail(lead.getEmail());
        dto.setWebsite(lead.getWebsite());
        dto.setPhone(lead.getPhone());
        dto.setFax(lead.getFax());
        dto.setInvoiceAddress(lead.getInvoiceAddress());

        dto.setIncome(lead.getIncome());
        dto.setIncomeSource(lead.getIncomeSource());
        dto.setOtherIncome(lead.getOtherIncome());
        dto.setOtherIncomeSource(lead.getOtherIncomeSource());
        dto.setBudget(lead.getBudget());
        dto.setNotes(lead.getNotes());
        dto.setArea(lead.getArea());

        // IDs
        dto.setStateId(lead.getStateId());
        dto.setDistId(lead.getDistId());
        dto.setCityId(lead.getCityId());

        // ✅ FETCH STATE NAME USING stateId
        if (lead.getStateId() != null) {
            stateRepository.findById(lead.getStateId().longValue())
                    .ifPresent(state -> {
                        dto.setStateName(state.getName());
                    });
        }

        dto.setUserId(lead.getUserId());
        dto.setBranchId(lead.getBranchId());
        dto.setOrgId(lead.getOrgId());

        dto.setIsActive(lead.getIsActive());

        return dto;
    }

    @Transactional
    public Map<String, Object> bulkImportLeads(List<LeadDto> dtos) {

        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> results = new ArrayList<>();

        int saved = 0;
        int failed = 0;

        for (LeadDto dto : dtos) {

            Map<String, String> result = new HashMap<>();
            result.put("name", dto.getCustomerName());
            result.put("contactNo1", String.valueOf(dto.getContactNo()));

            boolean exists = leadRepository
                    .existsByCustomerNameIgnoreCaseAndContactNo(dto.getCustomerName(), dto.getContactNo());

            if (exists) {
                failed++;
                result.put("status", "FAILED");
                result.put("error", "Lead already exists");
            } else {
                Lead lead = new Lead();

                lead.setDate(dto.getDate());
                lead.setCustomerName(dto.getCustomerName());
                lead.setContactNo(dto.getContactNo());
                lead.setEmail(dto.getEmail());
                lead.setBudget(dto.getBudget());
                lead.setIncome(dto.getIncome());
                lead.setIncomeSource(dto.getIncomeSource());
                lead.setInvoiceAddress(dto.getInvoiceAddress());
                lead.setIsActive(dto.getIsActive());
                lead.setCreatedDate(new Date());

                leadRepository.save(lead);
                saved++;

                result.put("status", "SUCCESS");
            }

            results.add(result);
        }

        response.put("savedCount", saved);
        response.put("errorCount", failed);
        response.put("results", results);

        return response;
    }

    @Override
    public LeadDto createFromInquiry(InquiryLeadCreateDto dto) {

        Lead lead = new Lead();

        lead.setCustomerName(dto.getCustomerName());

        if (dto.getContactNo() != null) {
            lead.setContactNo(dto.getContactNo());
        }

        if (dto.getPanNo() != null && !dto.getPanNo().isEmpty()) {
            lead.setPanNo(dto.getPanNo());
        }

        // ✅ NULL SAFE
        if (dto.getStateId() != null) {
            lead.setStateId(dto.getStateId());
        }

        if (dto.getCityId() != null) {
            lead.setCityId(dto.getCityId());
        }

        lead.setBudget(dto.getBudget() != null ? dto.getBudget() : 0);

        // 🔥 DEFAULTS
        lead.setDate(new Date());
        lead.setIsActive(1);
        lead.setOrgId(1);
        lead.setBranchId(1);
        lead.setUserId(1);
        lead.setCreatedDate(new Date());

        Lead saved = leadRepository.save(lead);
        return mapToDto(saved);
    }

}
