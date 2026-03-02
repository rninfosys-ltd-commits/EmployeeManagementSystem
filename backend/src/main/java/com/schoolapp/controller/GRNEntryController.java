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

import com.schoolapp.entity.GRNEntry;
import com.schoolapp.service.GRNEntryService;

@RestController
@RequestMapping("/GRNEntry")

public class GRNEntryController {
	@Autowired
	GRNEntryService GRNEntryService;

	@PostMapping("/save")
	public List<GRNEntry> saveGRNEntry(@RequestBody List<GRNEntry> GRNEntry)
			throws ClassNotFoundException, SQLException {

		return GRNEntryService.saveGRNEntry(GRNEntry);
	}

	@PostMapping("/getAll")
	public String getAllGRNEntry(@RequestBody GRNEntry GRNEntry) throws Exception {

		return GRNEntryService.getAllGRNEntry(GRNEntry);
	}

	@PostMapping("/getAllStudentClassWise")
	public String getAllStudentClassWise(@RequestBody GRNEntry GRNEntry) throws Exception {
		return GRNEntryService.getAllStudentClassWise(GRNEntry);
	}

	@GetMapping("/get")
	public GRNEntry findGRNEntryById(@PathVariable int GRNEntryID) {
		return GRNEntryService.findGRNEntryById(GRNEntryID);
	}

	@PutMapping("/update")
	public GRNEntry updateDeleteGRNEntry(@RequestBody GRNEntry GRNEntry) throws ClassNotFoundException, SQLException {
		return GRNEntryService.updateDeleteGRNEntry(GRNEntry);
	}

	@DeleteMapping("/delete")
	public String deleteGRNEntryById(@RequestBody GRNEntry GRNEntry) {
		GRNEntryService.deleteGRNEntryById(GRNEntry.getGrnentryId());
		return "deleted............";
	}
}
