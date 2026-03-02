package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.entity.SubCast;

@Service
public class SubCastService {
	@Autowired

	com.schoolapp.dao.SubCastDao SubCastDao;

	public SubCast saveSubCast(SubCast SubCast) {
		return SubCastDao.saveSubCast(SubCast);
	}

	public List<SubCast> getAllSubCast() {
		return SubCastDao.getAllSubCast();
	}

	public SubCast findSubCastById(int SubCastId) {
		return SubCastDao.findSubCastById(SubCastId);
	}

	public SubCast updateSubCast(SubCast SubCastId) {
		SubCast SubCast = SubCastDao.findSubCastById(SubCastId.getSubCastId());
		SubCast.setSubCastId(SubCastId.getSubCastId());
		SubCast.setSubCastName(SubCastId.getSubCastName());
		return SubCastDao.saveSubCast(SubCast);
	}

	public String deleteSubCastById(int SubCastId) {
		SubCastDao.deleteSubCastById(SubCastId);
		return "deleted";
	}
}
