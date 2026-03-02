package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.DemoEntity;
import com.schoolapp.repository.DemoRepo;

@Component
public class DemoDao {
	@Autowired
	DemoRepo demorepo;
	public DemoEntity save(DemoEntity de) {
		return demorepo.save(de);
	}
	
	public List<DemoEntity> getAll( ){
		return demorepo.findAll();
	}
	
	public DemoEntity findById(int de) {
		return demorepo.findById(de).get()	;
		}
	
}
