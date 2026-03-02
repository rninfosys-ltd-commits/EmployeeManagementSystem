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

import com.schoolapp.entity.InqueryMaster;
import com.schoolapp.service.InqueryMasterService;

@RestController
@RequestMapping("/InquiryMaster")

public class InqueryMasterDaoController {

	@Autowired

	InqueryMasterService inqueryMasterService;

	@PostMapping("/save")
	public String saveInqueryMaster(@RequestBody InqueryMaster inqueryMaster)
			throws ClassNotFoundException, SQLException {
		inqueryMasterService.saveInqueryMst(inqueryMaster);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<InqueryMaster> getAllInqueryMaster() {
		return inqueryMasterService.getAllInquermst();
	}

	@GetMapping("/get")
	public InqueryMaster findResoById(@RequestBody InqueryMaster inqueryMaster) {
		return inqueryMasterService.findInqueryById(inqueryMaster.getInqueryId());
		// return State;
	}

	@PutMapping("/updateDeleteInqueryMaster")
	public String updateDeleteContractor(@RequestBody InqueryMaster inqueryMaster)
			throws ClassNotFoundException, SQLException {
		inqueryMasterService.updateDeleteInqueryMaster(inqueryMaster);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateInquery(@RequestBody InqueryMaster inqueryMaster) throws ClassNotFoundException, SQLException {
		inqueryMasterService.updateInquery(inqueryMaster);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteIndqueryById(@RequestBody InqueryMaster inqueryMaster) {
		int id = inqueryMaster.getInqueryId();

		if (id > 0) {
			inqueryMasterService.deleteInqueryById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}
