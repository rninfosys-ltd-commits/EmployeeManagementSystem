package com.schoolapp.entity;

import java.util.Date;
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
public class WorkCompletion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int workCompletionId;
	private int workOrderId;
	private int workMasterId;
	private int srNo;
	private Date orderDate;
	private int contractorId;
	private int areaSqrFt;
	private int areaId;
	private int productId;
	private int completeSqrFt;
	private int userId;
	private int orgId;
	private int branchId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private int isActive;
}
