package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
import com.schoolapp.entity.Company;
import com.schoolapp.repository.CompanyRepo;

@Component
public class CompanyDao {
	@Autowired
	 
	CompanyRepo companyrepo;
	
	
	 
	  
	
	public Company save(Company c) {
		
		System.out.println(c.getBrandName());
		return companyrepo.save(c);
	}
	
	public List<Company> getAll(){
		return companyrepo.findAll()
;	}
	

}
