package com.schoolapp.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.TcMaster;
import com.schoolapp.service.TcMasterService;

@RestController
@RequestMapping("/tcMaster")

public class TcMasterController {
	@Autowired
	TcMasterService TcMasterService;

	@PostMapping("/save")
	public String saveTcMaster(@RequestBody TcMaster TcMaster) throws ClassNotFoundException, SQLException {
		TcMasterService.saveTcMaster(TcMaster);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllTcMaster(@RequestBody TcMaster TcMaster) throws ClassNotFoundException, SQLException {

		return TcMasterService.getAllTcMaster(TcMaster);
	}

	@PostMapping("/get")
	public TcMaster findTcMasterById(@RequestBody TcMaster TcMaster) {

		return TcMasterService.findTcMasterById(TcMaster.getTcMasterId());
	}

	@PutMapping("/update")
	public String updateTcMaster(@RequestBody TcMaster TcMaster) throws ClassNotFoundException, SQLException {
		TcMasterService.updateTcMaster(TcMaster);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteTcMasterById(@RequestBody TcMaster TcMaster) {
		int id = TcMaster.getTcMasterId();
		if (id > 0) {
			TcMasterService.deleteTcMasterById(id);
			return "deleted..." + id;
		}

		return "Wrong Id" + id;
	}
}
