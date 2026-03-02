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

import com.schoolapp.entity.StdAnnualFees;
import com.schoolapp.entity.StdTransactionDetailes;
import com.schoolapp.service.StdTransactionDetailesService;

@RestController
@RequestMapping("/stdTransactionDetailes")

public class StdTransactionDetailesController {
	@Autowired
	StdTransactionDetailesService stdTransactionDetailesService;

	@PostMapping("/save")
	public List<StdTransactionDetailes> saveStdTransactionDetailes(
			@RequestBody List<StdTransactionDetailes> stdTransactionDetailes)
			throws ClassNotFoundException, SQLException {
		return stdTransactionDetailesService.saveStdTransactionDetailes(stdTransactionDetailes);
	}

	@PostMapping("/getPdfRcp")
	public String getPdfRcp(@RequestBody StdTransactionDetailes stdTransactionDetailes)
			throws ClassNotFoundException, SQLException {
		return stdTransactionDetailesService.getPdfRcp(stdTransactionDetailes);
	}

	@PostMapping("/getAll")
	public String getAllStdTransactionDetailes(@RequestBody StdTransactionDetailes stdTransactionDetailes)
			throws ClassNotFoundException, SQLException {
		return stdTransactionDetailesService.getAllStdTransactionDetailes(stdTransactionDetailes);
	}

	@PostMapping("/getAllTranMst")
	public String getAllTranMst(@RequestBody StdTransactionDetailes stdTransactionDetailes)
			throws ClassNotFoundException, SQLException {
		return stdTransactionDetailesService.getAllTranMst(stdTransactionDetailes);
	}

	@PostMapping("/stdYearwiseFeesTrnDetailes")
	public String stdYearwiseFeesTrnDetailes(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {
		return stdTransactionDetailesService.stdYearwiseFeesTrnDetailes(StdAnnualFees);
	}

	@GetMapping("/get")
	public StdTransactionDetailes findStdTransactionDetailesById(@PathVariable int stdTransactionDetailesId) {
		return stdTransactionDetailesService.findStdTransactionDetailesById(stdTransactionDetailesId);
	}

	@PutMapping("/update")
	public String updateStdTransactionDetailes(@RequestBody StdTransactionDetailes stdTransactionDetailes)
			throws ClassNotFoundException, SQLException {
		stdTransactionDetailesService.updateStdTransactionDetailes(stdTransactionDetailes);
		return "Record Updated.......";

	}

	@DeleteMapping("/delete")
	public String deleteStdTransactionDetailesById(@RequestBody StdTransactionDetailes stdTransactionDetailes) {
		stdTransactionDetailesService
				.deleteStdTransactionDetailesById(stdTransactionDetailes.getStdTransactionDetailesId());
		return "deleted............";
	}
}
