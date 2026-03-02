package com.schoolapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "km_batch")
public class KmBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "km_batch_no")
    private Long kmBatchNo;

    private Date trndate;
    private String createdby;

    @Builder.Default
    @Column(name = "approval_stage")
    private String approvalStage = "NONE";

    private String approval1;
    private String approval2;
    private String approval3;
    private String approval4; // rejection auditor userId

    @Transient
    private String approval1Name;

    @Transient
    private String approval2Name;

    @Transient
    private String approval3Name;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<KmDetails> entries;

    @Transient
    private Integer totalEntries;

    public Integer getTotalEntries() {
        return entries == null ? 0 : entries.size();
    }

}
