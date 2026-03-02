package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.TestEntity;
import com.schoolapp.repository.TestRepo;

@Component
public class TestDao {
	 @Autowired
	 TestRepo restrepo;
	 
	  public TestEntity saveAll(TestEntity testentity) {
		  return restrepo.save(testentity);
	  }
	  
	  public List<TestEntity> getAll() {
		  return restrepo.findAll();
	  }
	 
	

}
