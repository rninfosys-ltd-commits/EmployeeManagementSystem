package com.schoolapp.service;

import java.util.List;
import java.util.Map;

import com.schoolapp.dao.InquiryDto;
import com.schoolapp.dao.InquiryImportRequest;

public interface InquiryService {

    InquiryDto createInquiry(InquiryDto dto);

    List<InquiryDto> getAllInquiries();

    InquiryDto getInquiryById(int inquiryId);

    InquiryDto updateInquiry(int inquiryId, InquiryDto dto);

    void deleteInquiry(int inquiryId);

    Map<String, Object> importInquiries(InquiryImportRequest request);

}
