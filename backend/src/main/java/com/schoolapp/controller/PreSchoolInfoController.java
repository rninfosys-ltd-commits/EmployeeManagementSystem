package com.schoolapp.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.PreSchoolInfo;
import com.schoolapp.service.PreSchoolInfoService;

@RestController
@RequestMapping("/preSchoolInfo")

public class PreSchoolInfoController {
	@Autowired
	PreSchoolInfoService preSchoolInfoService;

	@PostMapping("/save")
	public String savePreSchoolInfo(@RequestBody PreSchoolInfo preSchoolInfo)
			throws ClassNotFoundException, SQLException {
		preSchoolInfoService.savePreSchoolInfo(preSchoolInfo);
		return "Record save successfully";

	}

	@PostMapping("/getAll")
	public String getAllPreSchoolInfo(@RequestBody PreSchoolInfo preSchoolInfo)
			throws ClassNotFoundException, SQLException {
		return preSchoolInfoService.getAllPreSchoolInfo(preSchoolInfo);
	}

	@GetMapping("/get")
	public PreSchoolInfo findPreSchoolInfoById(@PathVariable int preSchoolInfoId) {
		return preSchoolInfoService.findPreSchoolInfoById(preSchoolInfoId);
	}

	@PutMapping("/update")
	public String updatePreSchoolInfo(@RequestBody PreSchoolInfo preSchoolInfo)
			throws ClassNotFoundException, SQLException {
		preSchoolInfoService.updatePreSchoolInfo(preSchoolInfo);
		return "Record Updated.......";

	}

	@DeleteMapping("/delete")
	public String deletePreSchoolInfoById(@RequestBody PreSchoolInfo preSchoolInfo) {
		preSchoolInfoService.deletePreSchoolInfoById(preSchoolInfo.getPreSchoolInfoId());
		return "deleted............";
	}

}
