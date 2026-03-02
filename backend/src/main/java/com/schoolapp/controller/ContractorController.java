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

import com.schoolapp.entity.Contractor;
import com.schoolapp.service.ContractorService;

@RestController
@RequestMapping("/contractor")

public class ContractorController {
	@Autowired
	ContractorService contractorService;

	@PostMapping("/save")
	public String saveContractor(@RequestBody Contractor contractor) throws ClassNotFoundException, SQLException {
		contractorService.saveContractor(contractor);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<Contractor> getAllContractors() {
		return contractorService.getAllContractor();
	}

	@GetMapping("/get")
	public Contractor findContractorById(@RequestBody Contractor contractor) {

		return contractorService.findContractorById(contractor.getContractorId());
	}

	@PutMapping("/update")
	public String updateContractor(@RequestBody Contractor contractor) throws ClassNotFoundException, SQLException {
		contractorService.updateContractor(contractor);
		return "Record Updated.....";
	}

	@PutMapping("/updateDeleteContractor")
	public String updateDeleteContractor(@RequestBody Contractor contractor)
			throws ClassNotFoundException, SQLException {
		contractorService.updateDeleteContractor(contractor);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteContractorById(@RequestBody Contractor contractor) {
		int id = contractor.getContractorId();

		if (id > 0) {
			contractorService.deleteContractorById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}
