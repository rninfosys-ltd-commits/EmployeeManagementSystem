package com.schoolapp.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.RecieptPaymentTransaction;

@RestController
@RequestMapping("/recieptPaymentTransaction")

public class RecieptPaymentTransactionController {
	@Autowired
	com.schoolapp.service.RecieptPaymentTransactionService RecieptPaymentTransactionService;

	@PostMapping("/save")
	public String saveRecieptPaymentTransaction(@RequestBody RecieptPaymentTransaction RecieptPaymentTransaction)
			throws ClassNotFoundException, SQLException {
		RecieptPaymentTransactionService.saveRecieptPaymentTransaction(RecieptPaymentTransaction);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllRecieptPaymentTransaction(@RequestBody RecieptPaymentTransaction RecieptPaymentTransaction)
			throws ClassNotFoundException, SQLException {
		return RecieptPaymentTransactionService.getAllRecieptPaymentTransaction(RecieptPaymentTransaction);
	}

	@PostMapping("/getAcStsmt")
	public String getAcStsmt(@RequestBody RecieptPaymentTransaction RecieptPaymentTransaction)
			throws ClassNotFoundException, SQLException {
		return RecieptPaymentTransactionService.getAcStsmt(RecieptPaymentTransaction);
	}

}
