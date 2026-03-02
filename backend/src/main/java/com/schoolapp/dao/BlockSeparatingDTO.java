package com.schoolapp.dao;

//package com.yourapp.blockseparating.dto;

import java.util.Date;

public class BlockSeparatingDTO {

    private Long id;

    private String batchNumber;
    private Date castingDate;
    private String blockSize;
    private Integer time;
    private String remark;
    private String shift;
    public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	private int userId;
    private int branchId;
    private int orgId;
    
    private Date reportDate;

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }


    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public Date getCastingDate() { return castingDate; }
    public void setCastingDate(Date castingDate) { this.castingDate = castingDate; }

    public String getBlockSize() { return blockSize; }
    public void setBlockSize(String blockSize) { this.blockSize = blockSize; }

    public Integer getTime() { return time; }
    public void setTime(Integer time) { this.time = time; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public int getOrgId() { return orgId; }
    public void setOrgId(int orgId) { this.orgId = orgId; }
}

