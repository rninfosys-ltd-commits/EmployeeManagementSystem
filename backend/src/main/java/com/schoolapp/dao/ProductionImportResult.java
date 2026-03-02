package com.schoolapp.dao;

public class ProductionImportResult {

    private String batchNo;
    private String status;
    private String error;

    public ProductionImportResult(String batchNo, String status, String error) {
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
