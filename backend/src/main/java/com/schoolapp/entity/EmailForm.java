package com.schoolapp.entity;

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
public class EmailForm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int emailId;
	private String to_email;
	private String subject;
	private String body;

}
