package com.schoolapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.TrialDao;
import com.schoolapp.entity.TrialEntity;

@Service
public class TrialService {
	@Autowired
	TrialDao td;
	
	public TrialEntity saveCity(TrialEntity te) {
		return td.saveCity(te);
	}
	
	
	
}
