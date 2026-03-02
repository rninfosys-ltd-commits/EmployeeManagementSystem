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
public class TcMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tcMasterId;

	private String tcStdName;
	private String talukaName;
	private String distName;
	private String conduct;
	private Date dateOfLeaving;
	private String reasonOfLeaving;
	private String remark;
	private int lcissueNo;

	private int userId;
	private int orgId;
	private int branchId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private int isActive;
}
