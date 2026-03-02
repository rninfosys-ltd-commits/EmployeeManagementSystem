package com.schoolapp.dao;

import java.util.List;

public class WireCuttingImportRequestDto {

    private List<WireCuttingReportRequestDto> wireCuttings;

    public List<WireCuttingReportRequestDto> getWireCuttings() {
        return wireCuttings;
    }

    public void setWireCuttings(List<WireCuttingReportRequestDto> wireCuttings) {
        this.wireCuttings = wireCuttings;
    }
}
