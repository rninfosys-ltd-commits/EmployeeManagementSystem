package com.schoolapp.entity;

import jakarta.persistence.*;
//import lombok.Data;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_trn_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTrnDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long trbactno;
	private Date trndate;
	private String customerName;
	private String toolingdrawingpartno;
	private String partdrawingname;
	private String partdrawingno;
	private String descriptionoftooling;
	private String cmworkorderno;
	private String toolingassetno;
	private String createdby;

	private String status = "PENDING";

	@ManyToOne
	@JoinColumn(name = "bactno")
	private BatchDetails batchDetails;
}
