package com.schoolapp.dao;

import java.util.List;

public class CastingImportRequestDto {

    private List<CastingHallReportRequestDto> castings;

    public List<CastingHallReportRequestDto> getCastings() {
        return castings;
    }

    public void setCastings(List<CastingHallReportRequestDto> castings) {
        this.castings = castings;
    }
}
