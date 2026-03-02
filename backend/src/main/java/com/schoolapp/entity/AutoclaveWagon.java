//package com.crmemp.entity;
package com.schoolapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "autoclave_wagon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoclaveWagon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer eBatch;
    private Integer eSize;

    private Integer mBatch;
    private Integer mSize;

    private Integer wBatch;
    private Integer wSize;

    // 🔗 FK
    @ManyToOne
    @JoinColumn(name = "autoclave_id")
    private AutoclaveCycle autoclave;

    // 🔥 REQUIRED FIELDS
    private int userId;
    private int branchId;
    private int orgId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private int updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    private int isActive = 1;

}
