package com.schoolapp.dao;

public class BatchHeaderDto {

    private Long bactno;
    private String trndate;
    private String createdby;
    private Integer totalTransactions;
    private boolean approved;
    private String approvalStage;

    // NEW — real approver names
    private String aproval1Name;
    private String aproval2Name;
    private String aproval3Name;

    public Long getBactno() { return bactno; }
    public void setBactno(Long bactno) { this.bactno = bactno; }

    public String getTrndate() { return trndate; }
    public void setTrndate(String trndate) { this.trndate = trndate; }

    public String getCreatedby() { return createdby; }
    public void setCreatedby(String createdby) { this.createdby = createdby; }

    public Integer getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(Integer totalTransactions) { this.totalTransactions = totalTransactions; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public String getApprovalStage() { return approvalStage; }
    public void setApprovalStage(String approvalStage) { this.approvalStage = approvalStage; }

    public String getAproval1Name() { return aproval1Name; }
    public void setAproval1Name(String aproval1Name) { this.aproval1Name = aproval1Name; }

    public String getAproval2Name() { return aproval2Name; }
    public void setAproval2Name(String aproval2Name) { this.aproval2Name = aproval2Name; }

    public String getAproval3Name() { return aproval3Name; }
    public void setAproval3Name(String aproval3Name) { this.aproval3Name = aproval3Name; }
}
