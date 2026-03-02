package com.schoolapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.PurchaseOrderDto;
import com.schoolapp.service.PurchaseOrderService;

@RestController
@RequestMapping("/PurchaseOrder")
public class PurchaseOrderController {

	@Autowired
	PurchaseOrderService purchaseOrderService;

	@GetMapping("/getAll")
	public List<PurchaseOrderDto> getAllPurchaseOrder() throws Exception {
		return purchaseOrderService.getAllPurchaseOrder();
	}

	@GetMapping("/get")
	public PurchaseOrderDto findPurchaseOrderById(@RequestBody PurchaseOrderDto purchaseOrder) {
		return purchaseOrderService.findPurchaseOrderById(purchaseOrder.getId());
	}

	@PutMapping("/update")
	public String updatePurchaseOrder(@RequestBody PurchaseOrderDto purchaseOrder) {
		// Updated status logic if needed
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deletePurchaseOrderById(@RequestBody PurchaseOrderDto purchaseOrder) {
		Long id = purchaseOrder.getId();

		if (id != null && id > 0) {
			purchaseOrderService.deletePurchaseOrderById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}
