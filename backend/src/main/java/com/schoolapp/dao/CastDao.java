package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Cast;

@Component
public class CastDao {
	@Autowired
	com.schoolapp.repository.CastRepo CastRepo;

	public Cast saveCast(Cast Cast) {
		return CastRepo.save(Cast);
	}

	public List<Cast> getAllCast() {
		return CastRepo.findAll();
	}

	public Cast findCastById(int CastId) {
		return CastRepo.findById(CastId).get();
	}

	public String deleteCastById(int CastId) {
		CastRepo.deleteById(CastId);
		return "deleted";
	}
}
