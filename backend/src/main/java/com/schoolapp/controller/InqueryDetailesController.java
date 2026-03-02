package com.schoolapp.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.InqueryDetailes;
import com.schoolapp.service.InqueryDetailesService;

@RestController
@RequestMapping("/InquiryDetails")

public class InqueryDetailesController {
	@Autowired
	InqueryDetailesService inqueryDetailesService;

	@PostMapping("/save")
	public String saveInqueryDetailes(@RequestBody InqueryDetailes inqueryDetailes)
			throws ClassNotFoundException, SQLException {
		inqueryDetailesService.saveInqueryDetailes(inqueryDetailes);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<InqueryDetailes> getAllInqueryDetailes() throws Exception {
		return inqueryDetailesService.getAllInqueryDetailes();
	}

	//
	@GetMapping("/get")
	public InqueryDetailes findInqueryDetailesById(@RequestBody InqueryDetailes inqueryDetailes) {

		return inqueryDetailesService.findInqueryDetailesById(inqueryDetailes.getInqueryDetailesId());
		// return State;
	}

	@PutMapping("/updateDeleteInqueryDetailes")
	public String updateDeleteInqueryDetailes(@RequestBody InqueryDetailes inqueryDetailes)
			throws ClassNotFoundException, SQLException {
		inqueryDetailesService.updateDeleteInqueryDetailes(inqueryDetailes);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateInqueryDetailes(@RequestBody InqueryDetailes inqueryDetailes)
			throws ClassNotFoundException, SQLException {
		inqueryDetailesService.updateInqueryDetailes(inqueryDetailes);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteInqueryDetailesById(@RequestBody InqueryDetailes inqueryDetailes) {
		int id = inqueryDetailes.getInqueryDetailesId();

		if (id > 0) {
			inqueryDetailesService.deleteInqueryDetailesById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}
