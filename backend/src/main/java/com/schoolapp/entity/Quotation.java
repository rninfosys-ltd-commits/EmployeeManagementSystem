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
public class Quotation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int quotationId;
	private Date quotationDate;
	private int quotationMstId;
	private int customerId;
	private int productId;
	private String partiCulars;
	private float rate;
	private float quantity;
	private float discount;
	private float amount;
	private float total;
	private int grandTotal;
	private float mrp;
	private float scheme;
	private float cgst;
	private float sgst;
	private float cgstPer;
	private float sgstPer;
	private float igst;
	private float igstPer;
	private int userId;
	private int orgId;
	private int branchId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private int isActive;
}
