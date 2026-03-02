package com.schoolapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.InformationDao;
import com.schoolapp.entity.Information;

@Service
public class InformationService {
	@Autowired
	InformationDao informationdao;
	public Information saveAll(Information info) {
		return informationdao.saveAll(info);
		
	}
	 
}
