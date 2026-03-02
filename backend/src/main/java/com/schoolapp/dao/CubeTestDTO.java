package com.schoolapp.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class CubeTestDTO {
	private Integer userId;

    private Long id;
    private String batchNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date castDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testingDate;

    private String shift;

    private String cubeDimensionImmediate;
    private String cubeDimensionOverDry;

    private String weightImmediateKg;
    private String weightOverDryKg;

    private Double weightWithMoistureKg;

    private Double loadOverDryTonn;
    private Double loadMoistureTonn;

    private Double compStrengthOverDry;
    private Double compStrengthMoisture;

    private Double densityKgM3;

    private int isActive;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public Date getCastDate() { return castDate; }
    public void setCastDate(Date castDate) { this.castDate = castDate; }

    public Date getTestingDate() { return testingDate; }
    public void setTestingDate(Date testingDate) { this.testingDate = testingDate; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public String getCubeDimensionImmediate() { return cubeDimensionImmediate; }
    public void setCubeDimensionImmediate(String cubeDimensionImmediate) { this.cubeDimensionImmediate = cubeDimensionImmediate; }

    public String getCubeDimensionOverDry() { return cubeDimensionOverDry; }
    public void setCubeDimensionOverDry(String cubeDimensionOverDry) { this.cubeDimensionOverDry = cubeDimensionOverDry; }

    public String getWeightImmediateKg() { return weightImmediateKg; }
    public void setWeightImmediateKg(String weightImmediateKg) { this.weightImmediateKg = weightImmediateKg; }

    public String getWeightOverDryKg() { return weightOverDryKg; }
    public void setWeightOverDryKg(String weightOverDryKg) { this.weightOverDryKg = weightOverDryKg; }

    public Double getWeightWithMoistureKg() { return weightWithMoistureKg; }
    public void setWeightWithMoistureKg(Double weightWithMoistureKg) { this.weightWithMoistureKg = weightWithMoistureKg; }

    public Double getLoadOverDryTonn() { return loadOverDryTonn; }
    public void setLoadOverDryTonn(Double loadOverDryTonn) { this.loadOverDryTonn = loadOverDryTonn; }

    public Double getLoadMoistureTonn() { return loadMoistureTonn; }
    public void setLoadMoistureTonn(Double loadMoistureTonn) { this.loadMoistureTonn = loadMoistureTonn; }

    public Double getCompStrengthOverDry() { return compStrengthOverDry; }
    public void setCompStrengthOverDry(Double compStrengthOverDry) { this.compStrengthOverDry = compStrengthOverDry; }

    public Double getCompStrengthMoisture() { return compStrengthMoisture; }
    public void setCompStrengthMoisture(Double compStrengthMoisture) { this.compStrengthMoisture = compStrengthMoisture; }

    public Double getDensityKgM3() { return densityKgM3; }
    public void setDensityKgM3(Double densityKgM3) { this.densityKgM3 = densityKgM3; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

    
    
}
