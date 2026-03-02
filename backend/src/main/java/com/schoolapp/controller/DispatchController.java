package com.schoolapp.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.Dispatch;
import com.schoolapp.service.DispatchService;

@RestController
@RequestMapping("/Dispatch")

public class DispatchController {
	@Autowired
	DispatchService DispatchService;

	@PostMapping("/save")
	public List<Dispatch> saveDispatch(@RequestBody List<Dispatch> Dispatch)
			throws ClassNotFoundException, SQLException {

		return DispatchService.saveDispatch(Dispatch);
	}

	@PostMapping("/getAll")
	public String getAllDispatch(@RequestBody Dispatch Dispatch) throws Exception {

		return DispatchService.getAllDispatch(Dispatch);
	}

	@PostMapping("/getAllStudentClassWise")
	public String getAllStudentClassWise(@RequestBody Dispatch Dispatch) throws Exception {
		return DispatchService.getAllStudentClassWise(Dispatch);
	}

	@GetMapping("/get")
	public Dispatch findDispatchById(@PathVariable int DispatchID) {
		return DispatchService.findDispatchById(DispatchID);
	}

	@PutMapping("/update")
	public Dispatch updateDeleteDispatch(@RequestBody Dispatch Dispatch) throws ClassNotFoundException, SQLException {
		return DispatchService.updateDeleteDispatch(Dispatch);
	}

	@DeleteMapping("/delete")
	public String deleteDispatchById(@RequestBody Dispatch Dispatch) {
		DispatchService.deleteDispatchById(Dispatch.getDispatchId());
		return "deleted............";
	}
}
