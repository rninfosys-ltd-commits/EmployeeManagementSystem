package com.schoolapp.service;

import java.util.List;

import com.schoolapp.dao.InquiryScheduleDto;

public interface InquiryScheduleService {

    InquiryScheduleDto save(InquiryScheduleDto dto);

    InquiryScheduleDto update(Long id, InquiryScheduleDto dto);

    List<InquiryScheduleDto> getAll();

    InquiryScheduleDto getById(Long id);

    void delete(Long id);
}
