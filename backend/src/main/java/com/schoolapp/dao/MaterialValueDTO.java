package com.schoolapp.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialValueDTO {
    private Long materialMasterId;
    private String materialName;
    private String unit;
    private Double value;
    private int displayOrder;
}
