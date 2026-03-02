package com.schoolapp.dao;

import java.util.List;

public class ProductionImportResponse {

    private int savedCount;
    private int errorCount;
    private List<ProductionImportResult> results;

    public ProductionImportResponse(
            int savedCount,
            int errorCount,
            List<ProductionImportResult> results) {
        this.savedCount = savedCount;
        this.errorCount = errorCount;
        this.results = results;
    }

    public int getSavedCount() {
        return savedCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public List<ProductionImportResult> getResults() {
        return results;
    }
}
