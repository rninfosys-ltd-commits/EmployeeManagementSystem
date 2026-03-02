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

import com.schoolapp.entity.Quotation;
import com.schoolapp.service.QuotationService;

@RestController
@RequestMapping("/Quotation")

public class QuotationController {

	@Autowired
	QuotationService quotationService;

	@PostMapping("/save")
	public List<Quotation> saveQuotation(@RequestBody List<Quotation> quotation)
			throws ClassNotFoundException, SQLException {

		return quotationService.saveQuotation(quotation);
	}

	@GetMapping("/getAll")
	public List<Quotation> getAllQuotation() throws Exception {
		return quotationService.getAllQuotation();
	}

	//
	@GetMapping("/get")
	public Quotation findQuotationById(@RequestBody Quotation quotation) {

		return quotationService.findQuotationById(quotation.getQuotationId());
		// return State;
	}

	@PutMapping("/updateDeleteQuotation")
	public String updateDeleteQuotation(@RequestBody Quotation quotation)
			throws ClassNotFoundException, SQLException {
		quotationService.updateDeleteQuotation(quotation);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateQuotation(@RequestBody Quotation quotation)
			throws ClassNotFoundException, SQLException {
		quotationService.updateQuotation(quotation);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteQuotationById(@RequestBody Quotation quotation) {
		int id = quotation.getQuotationId();

		if (id > 0) {
			quotationService.deleteQuotationById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}
