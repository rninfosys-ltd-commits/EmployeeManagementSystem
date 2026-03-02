package com.schoolapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.TestEntity;
import com.schoolapp.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	TestService testservice;
	
	@PostMapping("/save")
	public TestEntity saveAll(@RequestBody TestEntity te) {
		return testservice.saveAll(te);
	}
	@GetMapping("/getAll")
	public List<TestEntity> getAll(){
		return testservice.getAll();
	}
	
	
}
