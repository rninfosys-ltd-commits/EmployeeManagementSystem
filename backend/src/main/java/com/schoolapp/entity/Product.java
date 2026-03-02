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
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productId;
	private String name;
	private String unit;
	private float quantity;
	private float rate;
	private float d1;
	private float d2;
	private String itmType;
	private String shrtnm;
	private String productdiscription;
	private float cgst;
	private float sgst;
	private float igst;
	private float purchaseRate;
	private String hsncode;
	private int category;
	private int categoryID;
	private int brandId;
	private float wrates;
	private int userId;
	private int brId;
	private int orgId;
	private int custId;
	private int isActive;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
}
