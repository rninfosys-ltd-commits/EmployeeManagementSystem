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
public class GRNEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int grnentryId;
	private int GRNEntryMasterId;
	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;

	private int updatedBy;
	private Date updatedDate;
	private int okQty;
	private String description;
	private int orderQty;
	private int rejectQty;
	private int actualQty;
	private int rate;
	private int productId;
	private int customerId;

}