package com.schoolapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.Information;
import com.schoolapp.service.InformationService;

@RestController
@RequestMapping("/Info")

public class InformationController {
	@Autowired
	InformationService informationservice;

	@PostMapping("/save")
	public Information saveAll(@RequestBody Information info) {

		System.out.println("hello");
		System.out.print("");
		return informationservice.saveAll(info);
	}

}