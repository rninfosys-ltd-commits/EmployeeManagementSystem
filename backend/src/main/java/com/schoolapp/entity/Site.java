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
public class Site {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int siteId;
	private int srNo;
	private String siteName;
	private String siteAddress;
	private int projectId;
	private int orgId;
	private int branchId;
	private int userId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private int isActive;
}
