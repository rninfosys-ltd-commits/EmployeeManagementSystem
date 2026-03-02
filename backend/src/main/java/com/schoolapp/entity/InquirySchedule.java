package com.schoolapp.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inquiry_schedule")
public class InquirySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inqId; // ✅ Schedule ID (NO CHANGE)

    private Long inquiryId; // ✅ Inquiry ID (NEW)

    @Builder.Default
    private int orgId = 0;
    @Builder.Default
    private int branchId = 0;
    private int userId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private int updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Temporal(TemporalType.DATE)
    private Date scheduleDate;

    private String scheTime;
    private String remark;
    private String inqStatus;
    private int assignTo;

}
