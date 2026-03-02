package com.schoolapp.dao;

import java.util.Date;

public class ProjectsDto {

    private int projectId;
    private String projectName;
    private Date sanctionDate;
    private Date startDate;
    private Date endDate;
    private String srvGutNo;
    private String previousLandOwner;
    private String landOwner;
    private String builderName;
    private int reraNo;
    private String address;
    private int budgetAmt;
    private int orgId;
    private int branchId;
    private int userId;
    private int isActive;

    // ===== GETTERS & SETTERS =====
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public Date getSanctionDate() { return sanctionDate; }
    public void setSanctionDate(Date sanctionDate) { this.sanctionDate = sanctionDate; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getSrvGutNo() { return srvGutNo; }
    public void setSrvGutNo(String srvGutNo) { this.srvGutNo = srvGutNo; }

    public String getPreviousLandOwner() { return previousLandOwner; }
    public void setPreviousLandOwner(String previousLandOwner) { this.previousLandOwner = previousLandOwner; }

    public String getLandOwner() { return landOwner; }
    public void setLandOwner(String landOwner) { this.landOwner = landOwner; }

    public String getBuilderName() { return builderName; }
    public void setBuilderName(String builderName) { this.builderName = builderName; }

    public int getReraNo() { return reraNo; }
    public void setReraNo(int reraNo) { this.reraNo = reraNo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getBudgetAmt() { return budgetAmt; }
    public void setBudgetAmt(int budgetAmt) { this.budgetAmt = budgetAmt; }

    public int getOrgId() { return orgId; }
    public void setOrgId(int orgId) { this.orgId = orgId; }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }
}
