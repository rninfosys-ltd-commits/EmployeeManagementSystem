package com.schoolapp.dao;

import java.util.List;

public class WireCuttingImportResponseDto {

    private int savedCount;
    private int errorCount;
    private List<WireCuttingImportResultDto> results;

    public int getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(int savedCount) {
        this.savedCount = savedCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public List<WireCuttingImportResultDto> getResults() {
        return results;
    }

    public void setResults(List<WireCuttingImportResultDto> results) {
        this.results = results;
    }
}
