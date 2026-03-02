package com.schoolapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Information;
import com.schoolapp.repository.InformationRepo;
 
@Component
public class InformationDao {
	@Autowired
	InformationRepo informationrepo;
	
	public Information saveAll(Information info) {
		return informationrepo.save(info);
		
	}
	 

}
