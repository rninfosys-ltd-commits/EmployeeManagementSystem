package com.schoolapp.entity;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OutWorkMaterial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int OutWorkMaterialId;
	private int OutWorkMaterialMasterId;
	private int purchaseOrderId;
	private int productId;
	private int customerId;
	private int isshuQty;
	private int scrapQty;
	private int rate;
	private String description;
	private int tranType;
	private int referenceNo;

	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
}