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

import com.schoolapp.entity.PurchaseOrderMaster;

import com.schoolapp.service.PurchaseOrderMasterService;

@RestController
@RequestMapping("/PurchaseOrderMaster")

public class PurchaseOrderMasterController {

	@Autowired

	PurchaseOrderMasterService purchaseOrderMasterService;

	@PostMapping("/save")
	public String savePurchaseOrderMaster(@RequestBody PurchaseOrderMaster purchaseOrderMaster)
			throws ClassNotFoundException, SQLException {
		purchaseOrderMasterService.savePurchaseMst(purchaseOrderMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllPurchaseOrderMaster(@RequestBody PurchaseOrderMaster purchaseOrderMaster)
			throws ClassNotFoundException, SQLException {
		return purchaseOrderMasterService.getAllPurchaseMst(purchaseOrderMaster);
	}

	@GetMapping("/get")
	public PurchaseOrderMaster findResoById(@RequestBody PurchaseOrderMaster purchaseOrderMaster) {
		return purchaseOrderMasterService.findPurchaseById(purchaseOrderMaster.getPurchaseOrderMstId());
		// return State;
	}

	@PutMapping("/updateDeletePurchaseOrderMaster")
	public String updateDeleteContractor(@RequestBody PurchaseOrderMaster purchaseOrderMaster)
			throws ClassNotFoundException, SQLException {
		purchaseOrderMasterService.updateDeletePurchaseOrderMaster(purchaseOrderMaster);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updatePurchase(@RequestBody PurchaseOrderMaster purchaseOrderMaster)
			throws ClassNotFoundException, SQLException {
		purchaseOrderMasterService.updatePurchase(purchaseOrderMaster);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteIndqueryById(@RequestBody PurchaseOrderMaster purchaseOrderMaster) {
		int id = purchaseOrderMaster.getPurchaseOrderMstId();

		if (id > 0) {
			purchaseOrderMasterService.deletePurchaseById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}
