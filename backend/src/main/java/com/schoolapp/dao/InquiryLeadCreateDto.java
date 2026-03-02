package com.schoolapp.dao;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

public class InquiryLeadCreateDto {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    // ✅ OPTIONAL
    private Long contactNo;

    // ✅ OPTIONAL (NO VALIDATION)
    private String panNo;


    private Integer stateId;
    private Integer cityId;
    private Integer budget;

    // ===== getters & setters =====

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getContactNo() {
        return contactNo;
    }
    public void setContactNo(Long contactNo) {
        this.contactNo = contactNo;
    }

    // ✅ REQUIRED
    public String getPanNo() {
        return panNo;
    }
    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public Integer getStateId() {
        return stateId;
    }
    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getCityId() {
        return cityId;
    }
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getBudget() {
        return budget;
    }
    public void setBudget(Integer budget) {
        this.budget = budget;
    }
}
