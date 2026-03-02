package com.schoolapp.service;

//package com.esystem.esystem.service;

//import com.esystem.esystem.dto.LeadDto;
import java.util.List;
import java.util.Map;

import com.schoolapp.dao.InquiryLeadCreateDto;
import com.schoolapp.dao.LeadDto;

public interface LeadService {

    LeadDto createLead(LeadDto dto);

    List<LeadDto> getAllLeads();

    LeadDto getLeadById(int leadId);

    LeadDto updateLead(int leadId, LeadDto dto);

    void deleteLead(int leadId);

    Map<String, Object> bulkImportLeads(List<LeadDto> dtos);

    LeadDto createFromInquiry(InquiryLeadCreateDto dto);
}
