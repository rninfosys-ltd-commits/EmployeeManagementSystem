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
@Table(name = "leads", uniqueConstraints = {
		@UniqueConstraint(columnNames = "pan_no")
})
public class Lead {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leadId;

	@Temporal(TemporalType.DATE)
	private Date date;

	private String customerName;

	private long contactNo;
	@Column(name = "pan_no", unique = true, length = 10)
	private String panNo;

	private String gstNo;
	private String email;
	private String website;
	private long phone;
	private long fax;
	private String invoiceAddress;

	private int income;
	private String incomeSource;
	private int otherIncome;
	private String otherIncomeSource;
	private int budget;
	private String notes;
	private String area;

	private Integer stateId;
	private Integer distId;
	private Integer cityId;

	private int userId;
	private int branchId;
	private int orgId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	private int updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	private int isActive;

}
