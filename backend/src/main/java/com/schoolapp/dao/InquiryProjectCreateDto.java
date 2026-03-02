package com.schoolapp.dao;

//import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;

public class InquiryProjectCreateDto {

    @NotBlank(message = "Project name is required")
    private String projectName;

    private Integer budgetAmt;

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public Integer getBudgetAmt() { return budgetAmt; }
    public void setBudgetAmt(Integer budgetAmt) { this.budgetAmt = budgetAmt; }
}
