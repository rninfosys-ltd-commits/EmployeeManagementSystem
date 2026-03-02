package com.schoolapp.dao;

import java.util.List;

public class ProjectImportRequest {

    private List<ProjectsDto> projects;
    private String source;
    private int uploadedBy;

    public List<ProjectsDto> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectsDto> projects) {
        this.projects = projects;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(int uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
