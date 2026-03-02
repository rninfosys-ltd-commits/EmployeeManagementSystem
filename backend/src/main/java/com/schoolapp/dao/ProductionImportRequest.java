package com.schoolapp.dao;

import java.util.List;

public class ProductionImportRequest {

    private List<ProductionEntryRequest> productions;
    private int uploadedBy;
    private int branchId;
    private int orgId;

    public List<ProductionEntryRequest> getProductions() {
        return productions;
    }

    public void setProductions(List<ProductionEntryRequest> productions) {
        this.productions = productions;
    }

    public int getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(int uploadedBy) {
        this.uploadedBy = uploadedBy;
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
}
