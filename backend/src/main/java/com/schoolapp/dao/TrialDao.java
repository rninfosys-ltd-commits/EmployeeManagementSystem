package com.schoolapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.TrialEntity;
import com.schoolapp.repository.TrialRepo;

@Component
public class TrialDao {
	 @Autowired
	 TrialRepo tr;
	 
	 public TrialEntity saveCity(TrialEntity te) {
		 return tr.save(te);
	 }
	 
}
