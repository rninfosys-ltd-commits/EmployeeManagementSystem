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

// import java.util.List;

import com.schoolapp.entity.WorkOrder;
import com.schoolapp.service.WorkOrderService;

@RestController
@RequestMapping("/workOrder")

public class WorkOrderController {

	@Autowired
	WorkOrderService workOrderService;

	@PostMapping("/save")
	public String saveWorkOrder(@RequestBody List<WorkOrder> workOrder) throws ClassNotFoundException, SQLException {
		if (workOrder != null && !workOrder.isEmpty()) {
			workOrderService.saveWorkOrder(workOrder);
			System.out.println("Found record");
			return String.format("Added %d workOrder.", workOrder.size());
		} else {
			return "No record found";
		}
	}

	@GetMapping("/getAll")
	public String getAllWorkOrder() throws Exception {
		return workOrderService.getAllWorkOrder();
	}

	@PostMapping("/getAllWorkOrderById")
	public String findWorkOrderById(@RequestBody WorkOrder workOrder) throws Exception {
		return workOrderService.getAllWorkOrderByOrderId(workOrder.getSrNo());
	}

	@PostMapping("/getWorkOrderContractor")
	public String getWorkOrderContractor(@RequestBody WorkOrder workOrder) throws Exception {
		return workOrderService.getWorkOrderContractor(workOrder);
	}

	@PostMapping("/getWorkOrderProduct")
	public String getWorkOrderProduct(@RequestBody WorkOrder workOrder) throws Exception {
		return workOrderService.getWorkOrderProduct(workOrder);
	}

	@PostMapping("/getWorkOrderArea")
	public String getWorkOrderArea(@RequestBody WorkOrder workOrder) throws Exception {
		return workOrderService.getWorkOrderArea(workOrder);
	}

	@PutMapping("/update")
	public String updateWorkOrder(@RequestBody WorkOrder workOrder) throws ClassNotFoundException, SQLException {
		workOrderService.updateWorkOrder(workOrder);
		return "Record Updated.....";
	}

	@PutMapping("/updateDeleteWorkOrder")
	public String updateDeleteWorkOrder(@RequestBody WorkOrder workOrder) throws ClassNotFoundException, SQLException {
		workOrderService.updateDeleteWorkOrder(workOrder);
		return "Record Updated.....";
	}

	// http://192.168.43.146:8080/leadAccounts/getAll
	@DeleteMapping("/delete")
	public String deleteWorkOrderById(@RequestBody WorkOrder workOrder) {
		int id = workOrder.getWorkOrderId();

		if (id > 0) {
			workOrderService.deleteWorkOrderById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}
