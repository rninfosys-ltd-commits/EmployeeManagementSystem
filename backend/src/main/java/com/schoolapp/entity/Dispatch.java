package com.schoolapp.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dispatch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int DispatchId;
	private int DispatchMasterId;
	private int purchaseOrderId;
	private int productId;
	private int customerId;
	private int orderQty;
	private int actualQty;
	private int OkQty;
	private int rejectQty;
	private int rate;
	private String description;

	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;

}
