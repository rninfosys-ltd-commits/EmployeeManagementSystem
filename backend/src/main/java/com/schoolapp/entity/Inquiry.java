package com.schoolapp.entity;

//package com.esystem.esystem.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inquiries")
public class Inquiry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int inqueryId;

	private int inqStatusId;

	@Temporal(TemporalType.DATE)
	private Date inqueryDate;

	private int leadAccountId;
	private int projectCode;
	private int unitCode;
	private String particulars;
	private int rate;
	private int quantity;
	private int amount;
	private int total;

	private int orgId;
	private int branchId;
	private int userId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	private int updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	private int isActive;

}
