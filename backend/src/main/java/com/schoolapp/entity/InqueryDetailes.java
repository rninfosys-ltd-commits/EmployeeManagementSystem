package com.schoolapp.entity;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
// import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class InqueryDetailes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int inqueryDetailesId;
	private Date inqueryDate;
	private int inqueryMstNo;
	private int leadAccountId;
	private int productCode;
	private String partiCulars;
	private int rate;
	private int quantity;
	private int discount;
	private int amount;
	private int total;
	private int grandTotal;
	private int mrp;
	private int scheme;
	private int cgst;
	private int sgst;
	private int cgstPer;
	private int sgstPer;
	private int igst;
	private int igstPer;
	private int dcn;
	private int userId;
	private int orgId;
	private int branchId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private int isActive;

}
