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

import com.schoolapp.entity.Dashboard;
import com.schoolapp.entity.StdAnnualFees;
import com.schoolapp.service.DashboardService;

@RestController
@RequestMapping("/dashboard")

public class DashboardController {
	@Autowired
	DashboardService dashboardService;

	@PostMapping("/save")
	public String saveDashboard(@RequestBody Dashboard dashboard) {
		dashboardService.saveDashboard(dashboard);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<Dashboard> getAllDashboard() {
		return dashboardService.getAllDashboard();
	}

	@GetMapping("/get")
	public Dashboard findDashboardById(@RequestBody Dashboard dashboard) {

		return dashboardService.findDashboardById(dashboard.getDashId());
		// return State;
	}

	@PutMapping("/update")
	public String updateDashboard(@RequestBody Dashboard dashboard) {
		dashboardService.updateDashboard(dashboard);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteDashboardById(@RequestBody Dashboard dashboard) {
		int id = dashboard.getDashId();
		if (id > 0) {
			dashboardService.deleteDashboardById(id);
			return "deleted..." + id;
		}
		return "Wrong Id" + id;
	}

	// ---------------------------------
	@PostMapping("/AllStudentYearWise")
	public String AllStudentYearWise(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {

		return dashboardService.AllStudentYearWise(StdAnnualFees);
	}

	@PostMapping("/AllStudentClassWise")
	public String AllStudentClassWise(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {

		return dashboardService.AllStudentClassWise(StdAnnualFees);
	}

	@PostMapping("/TotalFeesClassWise")
	public String TotalFeesClassWise(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {

		return dashboardService.TotalFeesClassWise(StdAnnualFees);
	}

	@PostMapping("/StdResourceWisePendingAmt")
	public String StdResourceWisePendingAmt(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {

		return dashboardService.StdResourceWisePendingAmt(StdAnnualFees);
	}

	@PostMapping("/AnnualResourceTrStatus")
	public String AnnualResourceTrStatus(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {

		return dashboardService.AnnualResourceTrStatus(StdAnnualFees);
	}

	@PostMapping("/AnnualFeesStatus")
	public String AnnualFeesStatus(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {

		return dashboardService.AnnualFeesStatus(StdAnnualFees);
	}

	@PostMapping("/AnnualClassReasourceWiseStatus")
	public String AnnualClassReasourceWiseStatus(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {

		return dashboardService.AnnualClassReasourceWiseStatus(StdAnnualFees);
	}

	@PostMapping("/datewiseFeesCollRpt")
	public String datewiseFeesCollRpt(@RequestBody StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {
		return dashboardService.datewiseFeesCollRpt(StdAnnualFees);
	}

}
