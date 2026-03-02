package com.schoolapp.dao;

//package com.employeemanagement.dto;

public class CityResponseDto {

    private Long cityId;
    private String cityName;

    private Long talukaId;
    private String talukaName;

    private Long districtId;
    private String districtName;

    private Long stateId;
    private String stateName;

    // getters & setters
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public Long getTalukaId() { return talukaId; }
    public void setTalukaId(Long talukaId) { this.talukaId = talukaId; }

    public String getTalukaName() { return talukaName; }
    public void setTalukaName(String talukaName) { this.talukaName = talukaName; }

    public Long getDistrictId() { return districtId; }
    public void setDistrictId(Long districtId) { this.districtId = districtId; }

    public String getDistrictName() { return districtName; }
    public void setDistrictName(String districtName) { this.districtName = districtName; }

    public Long getStateId() { return stateId; }
    public void setStateId(Long stateId) { this.stateId = stateId; }

    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }
}

