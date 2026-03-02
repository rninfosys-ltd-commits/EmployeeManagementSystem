package com.schoolapp.entity;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Dashboard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dashId;
	private int userId;
	private int orgId;
	private int branchId;
	private String api;
	private Timestamp timeStamp;
	private Date date;

}
