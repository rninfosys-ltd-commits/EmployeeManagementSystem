package com.schoolapp.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.TransactionMaster;
import com.schoolapp.service.TransactionMasterService;

@RestController
@RequestMapping("/transactionMaster")

public class TransactionMasterController {
	@Autowired
	TransactionMasterService TransactionMasterService;

	@PostMapping("/save")
	public String saveTransactionMaster(@RequestBody TransactionMaster TransactionMaster) {
		TransactionMasterService.saveTransactionMaster(TransactionMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllTransactionMaster(@RequestBody TransactionMaster TransactionMaster)
			throws ClassNotFoundException, SQLException {
		return TransactionMasterService.getAllTransactionMaster(TransactionMaster);
	}

}
