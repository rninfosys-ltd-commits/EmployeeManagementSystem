package com.schoolapp.dao;

public class WireCuttingImportResultDto {

    private String batchNo;
    private String status; // SAVED / FAILED
    private String error;

    public WireCuttingImportResultDto() {}

    public WireCuttingImportResultDto(String batchNo, String status, String error) {
        this.batchNo = batchNo;
        this.status = status;
        this.error = error;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
