package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.TestDao;
import com.schoolapp.entity.TestEntity;

@Service
public class TestService {
 @Autowired
 TestDao testdao;
 
 public TestEntity saveAll(TestEntity testentity) {
	 return testdao.saveAll(testentity);
	 
 }
 public List<TestEntity> getAll(){
	 return testdao.getAll();
 }
}