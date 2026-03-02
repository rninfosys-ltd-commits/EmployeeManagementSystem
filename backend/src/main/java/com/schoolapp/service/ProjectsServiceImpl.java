package com.schoolapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolapp.dao.InquiryProjectCreateDto;
import com.schoolapp.dao.ProjectsDto;
import com.schoolapp.entity.Project;
import com.schoolapp.repository.ProjectRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectRepository repository;

    public ProjectsServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    // ================= CREATE =================
    @Override
    public ProjectsDto create(ProjectsDto dto) {

        if (repository.existsByProjectNameIgnoreCase(dto.getProjectName())) {
            throw new RuntimeException("Project name already exists");
        }

        Project project = new Project();
        mapDtoToEntity(dto, project);
        project.setCreatedDate(new Date());

        return mapToDto(repository.save(project));
    }

    // ================= UPDATE =================
    @Override
    public ProjectsDto update(int id, ProjectsDto dto) {

        Project existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        repository.findByProjectNameIgnoreCase(dto.getProjectName())
                .ifPresent(p -> {
                    if (p.getProjectId() != id) {
                        throw new RuntimeException("Project name already exists");
                    }
                });

        mapDtoToEntity(dto, existing);
        existing.setUpdatedDate(new Date());

        return mapToDto(repository.save(existing));
    }

    // ================= GET ALL =================
    @Override
    public List<ProjectsDto> getAll() {

        List<Project> projects = repository.findAll();

        return projects.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ================= BULK IMPORT (EXCEL) =================
    @Override
    @Transactional
    public Map<String, Object> bulkCreate(List<ProjectsDto> dtos) {

        int savedCount = 0;
        int errorCount = 0;

        List<Map<String, Object>> results = new ArrayList<>();

        // Existing project names (case-insensitive)
        Set<String> existingNames = repository.findAll()
                .stream()
                .map(p -> p.getProjectName().toLowerCase())
                .collect(Collectors.toSet());

        for (ProjectsDto dto : dtos) {

            Map<String, Object> rowResult = new HashMap<>();
            rowResult.put("projectName", dto.getProjectName());

            try {

                // 🔍 VALIDATION
                if (dto.getProjectName() == null || dto.getProjectName().trim().isEmpty()) {
                    errorCount++;
                    rowResult.put("status", "FAILED");
                    rowResult.put("error", "Project name is required");
                    results.add(rowResult);
                    continue;
                }

                String projectName = dto.getProjectName().toLowerCase();

                // 🔍 DUPLICATE CHECK
                if (existingNames.contains(projectName)) {
                    errorCount++;
                    rowResult.put("status", "FAILED");
                    rowResult.put("error", "Project name already exists");
                } else {

                    Project project = new Project();
                    mapDtoToEntity(dto, project);
                    project.setCreatedDate(new Date());

                    repository.save(project);

                    savedCount++;
                    existingNames.add(projectName);

                    rowResult.put("status", "SUCCESS");
                }

            } catch (Exception e) {
                errorCount++;
                rowResult.put("status", "FAILED");
                rowResult.put("error", "Unexpected error");
            }

            results.add(rowResult);
        }

        // 🔥 FINAL RESPONSE
        Map<String, Object> response = new HashMap<>();
        response.put("savedCount", savedCount);
        response.put("errorCount", errorCount);
        response.put("results", results);

        return response;
    }

    // ================= MAPPERS =================
    private void mapDtoToEntity(ProjectsDto dto, Project project) {
        project.setProjectName(dto.getProjectName());
        project.setSanctionDate(dto.getSanctionDate());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setSrvGutNo(dto.getSrvGutNo());
        project.setPreviousLandOwner(dto.getPreviousLandOwner());
        project.setLandOwner(dto.getLandOwner());
        project.setBuilderName(dto.getBuilderName());
        project.setReraNo(dto.getReraNo());
        project.setAddress(dto.getAddress());
        project.setBudgetAmt(dto.getBudgetAmt());
        project.setOrgId(dto.getOrgId());
        project.setBranchId(dto.getBranchId());
        project.setUserId(dto.getUserId());
        project.setIsActive(dto.getIsActive());
    }

    private ProjectsDto mapToDto(Project project) {
        ProjectsDto dto = new ProjectsDto();
        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        dto.setSanctionDate(project.getSanctionDate());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setSrvGutNo(project.getSrvGutNo());
        dto.setPreviousLandOwner(project.getPreviousLandOwner());
        dto.setLandOwner(project.getLandOwner());
        dto.setBuilderName(project.getBuilderName());
        dto.setReraNo(project.getReraNo());
        dto.setAddress(project.getAddress());
        dto.setBudgetAmt(project.getBudgetAmt());
        dto.setOrgId(project.getOrgId());
        dto.setBranchId(project.getBranchId());
        dto.setUserId(project.getUserId());
        dto.setIsActive(project.getIsActive());
        return dto;
    }

    public ProjectsDto createFromInquiry(InquiryProjectCreateDto dto) {

        Project project = new Project();

        project.setProjectName(dto.getProjectName());
        project.setBudgetAmt(dto.getBudgetAmt() != null ? dto.getBudgetAmt() : 0);

        project.setOrgId(1);
        project.setBranchId(1);
        project.setUserId(1);
        project.setIsActive(1);
        project.setCreatedDate(new Date());

        return mapToDto(repository.save(project));
    }

}
