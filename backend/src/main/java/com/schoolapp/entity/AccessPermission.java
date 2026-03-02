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
public class AccessPermission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int accessPermissionId;
	private int accessUserId;
	private int modelId;
	private int accessCreate;
	private int accessRead;
	private int accessUpdate;
	private int accessDelete;
	private int userId;
	private int orgId;
	private int branchId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private int isActive;

}
