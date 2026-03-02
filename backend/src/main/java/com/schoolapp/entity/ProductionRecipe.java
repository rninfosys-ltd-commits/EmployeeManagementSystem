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
public class ProductionRecipe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productionRecipeId;
	private int productionRecipeMasterId;
	private int productId;
	private int rawMaterialId;
	private float quantity;
	private float rate;
	private float amount;
	private String description;

	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
}
