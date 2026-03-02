package com.schoolapp.dao;

public class CastingImportResult {

    private String batchNo;
    private String status;
    private String error;

    public CastingImportResult(String batchNo, String status, String error) {
        this.batchNo = batchNo;
        this.status = status;
        this.error = error;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
