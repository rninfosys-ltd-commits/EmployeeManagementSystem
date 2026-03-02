package com.schoolapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.Company;
import com.schoolapp.service.CompanyService;

@RestController
@RequestMapping("/company")

public class CompanyController {
	@Autowired

	CompanyService companyservice;

	@PostMapping("/save")
	public Company save(@RequestBody Company c) {
		return companyservice.save(c);
	}

	@GetMapping("/getall")
	public List<Company> getAll() {
		return companyservice.getAll();
	}

}
