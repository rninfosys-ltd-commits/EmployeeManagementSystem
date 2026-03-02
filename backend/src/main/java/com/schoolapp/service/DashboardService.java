package com.schoolapp.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.DashboardDao;
import com.schoolapp.entity.Dashboard;
import com.schoolapp.entity.StdAnnualFees;

@Service
public class DashboardService {
	@Autowired

	DashboardDao dashboardDao;

	public Dashboard saveDashboard(Dashboard dashboard) {
		return dashboardDao.saveDashboard(dashboard);
	}

	public List<Dashboard> getAllDashboard() {
		return dashboardDao.getAllDashboard();
	}

	public Dashboard findDashboardById(int dashId) {
		return dashboardDao.findDashboardById(dashId);
	}

	public Dashboard updateDashboard(Dashboard dashId) {

		return dashboardDao.saveDashboard(dashId);
	}

	public String deleteDashboardById(int dashId) {
		dashboardDao.deleteDashboardById(dashId);
		return "deleted";
	}

	public String AllStudentYearWise(StdAnnualFees StdAnnualFees) throws ClassNotFoundException, SQLException {

		return dashboardDao.AllStudentYearWise(StdAnnualFees);
	}

	public String AllStudentClassWise(StdAnnualFees StdAnnualFees) throws ClassNotFoundException, SQLException {

		return dashboardDao.AllStudentClassWise(StdAnnualFees);
	}

	public String TotalFeesClassWise(StdAnnualFees StdAnnualFees) throws ClassNotFoundException, SQLException {

		return dashboardDao.TotalFeesClassWise(StdAnnualFees);
	}

	public String StdResourceWisePendingAmt(StdAnnualFees StdAnnualFees) throws ClassNotFoundException, SQLException {

		return dashboardDao.StdResourceWisePendingAmt(StdAnnualFees);
	}

	public String AnnualFeesStatus(StdAnnualFees StdAnnualFees) throws ClassNotFoundException, SQLException {

		return dashboardDao.AnnualFeesStatus(StdAnnualFees);
	}

	public String AnnualResourceTrStatus(StdAnnualFees StdAnnualFees) throws ClassNotFoundException, SQLException {

		return dashboardDao.AnnualResourceTrStatus(StdAnnualFees);
	}

	public String AnnualClassReasourceWiseStatus(StdAnnualFees StdAnnualFees)
			throws ClassNotFoundException, SQLException {

		return dashboardDao.AnnualClassReasourceWiseStatus(StdAnnualFees);
	}

	public String datewiseFeesCollRpt(StdAnnualFees StdAnnualFees) throws ClassNotFoundException, SQLException {

		return dashboardDao.datewiseFeesCollRpt(StdAnnualFees);
	}
}
