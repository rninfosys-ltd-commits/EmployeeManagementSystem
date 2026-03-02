package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.entity.Cast;

@Service
public class CastService {
	@Autowired

	com.schoolapp.dao.CastDao CastDao;

	public Cast saveCast(Cast Cast) {
		return CastDao.saveCast(Cast);
	}

	public List<Cast> getAllCast() {
		return CastDao.getAllCast();
	}

	public Cast findCastById(int CastId) {
		return CastDao.findCastById(CastId);
	}

	public Cast updateCast(Cast CastId) {
		Cast Cast = CastDao.findCastById(CastId.getCastId());
		Cast.setCastId(CastId.getCastId());
		Cast.setCastName(CastId.getCastName());
		return CastDao.saveCast(Cast);
	}

	public String deleteCastById(int CastId) {
		CastDao.deleteCastById(CastId);
		return "deleted";
	}
}
