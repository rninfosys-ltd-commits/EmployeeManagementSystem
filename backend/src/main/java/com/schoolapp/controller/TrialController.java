package com.schoolapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.TrialEntity;
import com.schoolapp.service.TrialService;

@RestController
@RequestMapping("/ccity")

public class TrialController {
	@Autowired
	TrialService ts;

	@PostMapping("/save")
	public String saveCity(@RequestBody TrialEntity te) {
		ts.saveCity(te);
		return "record saved!!";
	}
}
