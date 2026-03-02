package com.schoolapp.entity;

import java.sql.Blob;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Information {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;

	@Lob
	private Blob imageBlob;

	public Blob getPhoto() {
		return imageBlob;
	}

	public void setPhoto(Blob imageBlob) {
		this.imageBlob = imageBlob;
	}

}
