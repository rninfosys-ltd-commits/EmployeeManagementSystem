package com.schoolapp.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.QuotationMaster;
import com.schoolapp.service.QuotationMasterService;

@RestController
@RequestMapping("/QuotationMaster")

public class QuotationMasterController {

	@Autowired

	QuotationMasterService quotationMasterService;

	@PostMapping("/save")
	public String saveQuotationMaster(@RequestBody QuotationMaster quotationMaster)
			throws ClassNotFoundException, SQLException {
		quotationMasterService.saveQuotationMst(quotationMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllQuotationMaster(@RequestBody QuotationMaster quotationMaster)
			throws ClassNotFoundException, SQLException {
		return quotationMasterService.getAllQuotationMst(quotationMaster);
	}

	@GetMapping("/get")
	public QuotationMaster findResoById(@RequestBody QuotationMaster quotationMaster) {
		return quotationMasterService.findQuotationById(quotationMaster.getQuotationMstId());
		// return State;
	}

	@PutMapping("/updateDeleteQuotationMaster")
	public String updateDeleteContractor(@RequestBody QuotationMaster quotationMaster)
			throws ClassNotFoundException, SQLException {
		quotationMasterService.updateDeleteQuotationMaster(quotationMaster);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateQuotation(@RequestBody QuotationMaster quotationMaster)
			throws ClassNotFoundException, SQLException {
		quotationMasterService.updateQuotation(quotationMaster);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteIndqueryById(@RequestBody QuotationMaster quotationMaster) {
		int id = quotationMaster.getQuotationMstId();

		if (id > 0) {
			quotationMasterService.deleteQuotationById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}
