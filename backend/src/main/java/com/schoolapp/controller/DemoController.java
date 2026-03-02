package com.schoolapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.DemoEntity;
import com.schoolapp.service.DemoService;

@RestController
@RequestMapping("/demoentity")

public class DemoController {
	@Autowired
	DemoService demoservice;

	@PostMapping("/save")
	public DemoEntity save(@RequestBody DemoEntity de) {
		return demoservice.save(de);
	}

	@GetMapping("/getAll")
	public List<DemoEntity> getAll() {
		return demoservice.getAll();
	}

	@GetMapping("/get")
	public DemoEntity findRecordById(@RequestBody DemoEntity de) {
		return demoservice.getRecordById(de.getId());
	}
}
