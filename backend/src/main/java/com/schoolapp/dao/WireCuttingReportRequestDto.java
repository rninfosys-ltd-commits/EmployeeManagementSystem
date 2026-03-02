package com.schoolapp.dao;

import java.util.Date;

public class WireCuttingReportRequestDto {

    private String batchNo;
    private Date cuttingDate;

    private int mouldNo;
    private int size;
    private int ballTestMm;

    private String otherReason;
    private String time;

    private int userId;
    private int branchId;
    private int orgId;
    private int updatedBy;
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Date getCuttingDate() {
		return cuttingDate;
	}
	public void setCuttingDate(Date cuttingDate) {
		this.cuttingDate = cuttingDate;
	}
	public int getMouldNo() {
		return mouldNo;
	}
	public void setMouldNo(int mouldNo) {
		this.mouldNo = mouldNo;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getBallTestMm() {
		return ballTestMm;
	}
	public void setBallTestMm(int ballTestMm) {
		this.ballTestMm = ballTestMm;
	}
	public String getOtherReason() {
		return otherReason;
	}
	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

    // 👉 Generate getters & setters
}
