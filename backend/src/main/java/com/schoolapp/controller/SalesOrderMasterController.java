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

import com.schoolapp.entity.SalesOrderMaster;
import com.schoolapp.service.SalesOrderMasterService;

@RestController
@RequestMapping("/SalesOrderMaster")

public class SalesOrderMasterController {

	@Autowired

	SalesOrderMasterService salesOrderMasterService;

	@PostMapping("/save")
	public String saveSalesOrderMaster(@RequestBody SalesOrderMaster salesOrderMaster)
			throws ClassNotFoundException, SQLException {
		salesOrderMasterService.saveSalesMst(salesOrderMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllSalesOrderMaster(@RequestBody SalesOrderMaster salesOrderMaster)
			throws ClassNotFoundException, SQLException {
		return salesOrderMasterService.getAllSalesMst(salesOrderMaster);
	}

	@GetMapping("/get")
	public SalesOrderMaster findResoById(@RequestBody SalesOrderMaster salesOrderMaster) {
		return salesOrderMasterService.findSalesById(salesOrderMaster.getSalesOrderMstId());
		// return State;
	}

	@PutMapping("/updateDeleteSalesOrderMaster")
	public String updateDeleteContractor(@RequestBody SalesOrderMaster salesOrderMaster)
			throws ClassNotFoundException, SQLException {
		salesOrderMasterService.updateDeleteSalesOrderMaster(salesOrderMaster);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateSales(@RequestBody SalesOrderMaster salesOrderMaster)
			throws ClassNotFoundException, SQLException {
		salesOrderMasterService.updateSales(salesOrderMaster);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteIndqueryById(@RequestBody SalesOrderMaster salesOrderMaster) {
		int id = salesOrderMaster.getSalesOrderMstId();

		if (id > 0) {
			salesOrderMasterService.deleteSalesById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}
