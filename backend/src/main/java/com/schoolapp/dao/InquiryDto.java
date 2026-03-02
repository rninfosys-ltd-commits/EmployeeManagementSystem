package com.schoolapp.dao;
//package com.esystem.esystem.dto;

import java.util.Date;

//package com.Crmemp.dto.request;

// import java.util.Date;

public class InquiryDto {

    private int inqueryId;
    private int inqStatusId;
    private Date inqueryDate;

    private int leadAccountId;
    private int projectCode;
    private int unitCode;
    private String particulars;
    private int rate;
    private int quantity;
    private int amount;
    private int total;

    private int orgId;
    private int branchId;
    private int userId;

    private int isActive;

    public InquiryDto() {
    }

    public int getInqueryId() {
        return inqueryId;
    }

    public void setInqueryId(int inqueryId) {
        this.inqueryId = inqueryId;
    }

    public int getInqStatusId() {
        return inqStatusId;
    }

    public void setInqStatusId(int inqStatusId) {
        this.inqStatusId = inqStatusId;
    }

    public Date getInqueryDate() {
        return inqueryDate;
    }

    public void setInqueryDate(Date inqueryDate) {
        this.inqueryDate = inqueryDate;
    }

    public int getLeadAccountId() {
        return leadAccountId;
    }

    public void setLeadAccountId(int leadAccountId) {
        this.leadAccountId = leadAccountId;
    }

    public int getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(int projectCode) {
        this.projectCode = projectCode;
    }

    public int getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
