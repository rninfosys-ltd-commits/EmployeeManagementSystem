package com.schoolapp.dao;

import java.util.List;

public class CastingImportResponse {

    private int savedCount;
    private int errorCount;
    private List<CastingImportResult> results;

    // ✅ REQUIRED CONSTRUCTOR
    public CastingImportResponse(
            int savedCount,
            int errorCount,
            List<CastingImportResult> results) {

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

    public List<CastingImportResult> getResults() {
        return results;
    }
}
