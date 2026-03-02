package com.schoolapp.dao;

import java.util.List;

public class ProductionEntryRequest {

	public String shift;

	public String siloNo1;
	public Double literWeight1;
	public Double faSolid1;

	public String siloNo2;
	public Double literWeight2;
	public Double faSolid2;

	// Legacy material fields (kept for backward compatibility)
	public Double waterLiter;
	public Double cementKg;
	public Double limeKg;
	public Double gypsumKg;
	public Double solOilKg;
	public Double aiPowerGm;
	public Double tempC;

	public String castingTime;
	public String productionTime;
	public String productionRemark;
	public String remark;

	public int userId;
	public int branchId;
	public int orgId;

	// ===== DYNAMIC MATERIALS =====
	public List<MaterialValueDTO> materials;

	// ===== GETTERS & SETTERS =====

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getSiloNo1() {
		return siloNo1;
	}

	public void setSiloNo1(String siloNo1) {
		this.siloNo1 = siloNo1;
	}

	public Double getLiterWeight1() {
		return literWeight1;
	}

	public void setLiterWeight1(Double literWeight1) {
		this.literWeight1 = literWeight1;
	}

	public Double getFaSolid1() {
		return faSolid1;
	}

	public void setFaSolid1(Double faSolid1) {
		this.faSolid1 = faSolid1;
	}

	public String getSiloNo2() {
		return siloNo2;
	}

	public void setSiloNo2(String siloNo2) {
		this.siloNo2 = siloNo2;
	}

	public Double getLiterWeight2() {
		return literWeight2;
	}

	public void setLiterWeight2(Double literWeight2) {
		this.literWeight2 = literWeight2;
	}

	public Double getFaSolid2() {
		return faSolid2;
	}

	public void setFaSolid2(Double faSolid2) {
		this.faSolid2 = faSolid2;
	}

	public Double getWaterLiter() {
		return waterLiter;
	}

	public void setWaterLiter(Double waterLiter) {
		this.waterLiter = waterLiter;
	}

	public Double getCementKg() {
		return cementKg;
	}

	public void setCementKg(Double cementKg) {
		this.cementKg = cementKg;
	}

	public Double getLimeKg() {
		return limeKg;
	}

	public void setLimeKg(Double limeKg) {
		this.limeKg = limeKg;
	}

	public Double getGypsumKg() {
		return gypsumKg;
	}

	public void setGypsumKg(Double gypsumKg) {
		this.gypsumKg = gypsumKg;
	}

	public Double getSolOilKg() {
		return solOilKg;
	}

	public void setSolOilKg(Double solOilKg) {
		this.solOilKg = solOilKg;
	}

	public Double getAiPowerGm() {
		return aiPowerGm;
	}

	public void setAiPowerGm(Double aiPowerGm) {
		this.aiPowerGm = aiPowerGm;
	}

	public Double getTempC() {
		return tempC;
	}

	public void setTempC(Double tempC) {
		this.tempC = tempC;
	}

	public String getCastingTime() {
		return castingTime;
	}

	public void setCastingTime(String castingTime) {
		this.castingTime = castingTime;
	}

	public String getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(String productionTime) {
		this.productionTime = productionTime;
	}

	public String getProductionRemark() {
		return productionRemark;
	}

	public void setProductionRemark(String productionRemark) {
		this.productionRemark = productionRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public List<MaterialValueDTO> getMaterials() {
		return materials;
	}

	public void setMaterials(List<MaterialValueDTO> materials) {
		this.materials = materials;
	}
}
