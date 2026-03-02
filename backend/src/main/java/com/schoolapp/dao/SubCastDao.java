package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.SubCast;

@Component
public class SubCastDao {
	@Autowired
	com.schoolapp.repository.SubCastRepo SubCastRepo;

	public SubCast saveSubCast(SubCast SubCast) {
		return SubCastRepo.save(SubCast);
	}

	public List<SubCast> getAllSubCast() {
		return SubCastRepo.findAll();
	}

	public SubCast findSubCastById(int SubCastId) {
		return SubCastRepo.findById(SubCastId).get();
	}

	public String deleteSubCastById(int SubCastId) {
		SubCastRepo.deleteById(SubCastId);
		return "deleted";
	}
}
