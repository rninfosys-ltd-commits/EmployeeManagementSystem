package com.schoolapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "km_details")
public class KmDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_no")
    @JsonIgnore
    private KmBatch batch;

    private String salesperson;
    private Double startKm;
    private Double endKm;
    private String visitedPlace;

    private String filePath;
    private String trnDate;

    public Long getBatchNo() {
        return batch == null ? null : batch.getKmBatchNo();
    }
}
