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

import com.schoolapp.entity.SalesOrder;
import com.schoolapp.service.SalesOrderService;

@RestController
@RequestMapping("/SalesOrder")

public class SalesOrderController {

	@Autowired
	SalesOrderService salesOrderService;

	@PostMapping("/save")
	public List<SalesOrder> saveSalesOrder(@RequestBody List<SalesOrder> salesOrder)
			throws ClassNotFoundException, SQLException {

		return salesOrderService.saveSalesOrder(salesOrder);
	}

	@GetMapping("/getAll")
	public List<SalesOrder> getAllSalesOrder() throws Exception {
		return salesOrderService.getAllSalesOrder();
	}

	//
	@GetMapping("/get")
	public SalesOrder findSalesOrderById(@RequestBody SalesOrder salesOrder) {

		return salesOrderService.findSalesOrderById(salesOrder.getSalesOrderId());
		// return State;
	}

	@PutMapping("/updateDeleteSalesOrder")
	public String updateDeleteSalesOrder(@RequestBody SalesOrder salesOrder)
			throws ClassNotFoundException, SQLException {
		salesOrderService.updateDeleteSalesOrder(salesOrder);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateSalesOrder(@RequestBody SalesOrder salesOrder)
			throws ClassNotFoundException, SQLException {
		salesOrderService.updateSalesOrder(salesOrder);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteSalesOrderById(@RequestBody SalesOrder salesOrder) {
		int id = salesOrder.getSalesOrderId();

		if (id > 0) {
			salesOrderService.deleteSalesOrderById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}
