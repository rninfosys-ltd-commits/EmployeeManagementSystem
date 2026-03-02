package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.CompanyDao;
import com.schoolapp.entity.Company;

@Service
public class CompanyService {
	@Autowired
	
	CompanyDao companydao;
	
	public Company save(Company c) {
		return companydao.save(c);
	}
	
	public List<Company> getAll(){
		return companydao.getAll();
	}

}
