package com.schoolapp.service;

//package com.Crmemp.services;

import java.util.List;
import java.util.Map;

import com.schoolapp.dao.InquiryProjectCreateDto;
import com.schoolapp.dao.ProjectsDto;

//import com.Crmemp.dto.request.InquiryProjectCreateDto;
//import com.Crmemp.dto.request.ProjectDto;

public interface ProjectsService {

    ProjectsDto create(ProjectsDto dto);

    ProjectsDto update(int id, ProjectsDto dto);

    List<ProjectsDto> getAll();

    Map<String, Object> bulkCreate(List<ProjectsDto> dtos);
   

    ProjectsDto createFromInquiry(InquiryProjectCreateDto dto);

}

