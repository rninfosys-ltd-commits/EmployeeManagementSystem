package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Unit;
import com.schoolapp.repository.UnitRepo;

@Component
public class UnitDao {

	@Autowired
	UnitRepo unitRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Unit saveUnit(Unit unit) {
		System.out.println("Data inserted successfully...");
		return unitRepo.save(unit);
	}

	public String updateUnit(Unit unit) {
		Integer unitId = unit.getUnitId();

		String unitName = unit.getUnitName();
		int floorId = unit.getFloorId();
		int wingId = unit.getWingId();
		int projectId = unit.getProjectId();
		int srNo = unit.getSrNo();
		float totoalSqrft = unit.getTotoalSqrft();
		int price = unit.getPrice();
		int userId = unit.getUserId();
		int orgId = unit.getOrgId();
		int branchId = unit.getBranchId();
		Date createdDate = unit.getCreatedDate();
		int updatedBy = unit.getUserId();
		Date updatedDate = unit.getUpdatedDate();
		int isActive = unit.getIsActive();

		String sql = "update unit set unit_id = ? , floor_id = ? , price = ? , project_id = ? , sr_no = ? , totoal_sqrft = ? , unit_name = ? ,"
				+ " wing_id = ? , branch_id = ? , created_date = ? , is_active = ? ,  updated_by = ? , updated_date = ? , user_id=?"
				+ " where unit_id= ? and org_id = ?";

		jdbcTemplate.update(sql, unitId, floorId, price, projectId, srNo, totoalSqrft, unitName, wingId, branchId,
				createdDate, isActive, updatedBy, updatedDate, userId, unitId, orgId);

		return "Record updated..!";
	}

	public Unit updateDeleteUnit(Unit unit) {
		Integer unitId = unit.getUnitId();

		int updatedBy = unit.getUpdatedBy();
		Date updatedDate = unit.getUpdatedDate();
		int orgId = unit.getOrgId();
		int isActive = unit.getIsActive();

		String sql = "update unit set is_active=?, updated_by=?,updated_date=? " + "where unit_id=? and org_id=?";
		jdbcTemplate.update(sql, isActive, updatedBy, updatedDate, unitId, orgId);
		return unitRepo.save(unit);
	}

	public List<Unit> getAllUnit() {
		return unitRepo.findAll();
	}

	public String getUnitSite(Unit unit) {
		int orgId = unit.getOrgId();
		int projectId = unit.getProjectId();

		String sql = "SELECT s.site_id, s.site_name FROM site s"
				+ " INNER JOIN project pj ON pj.org_id = s.org_id and pj.project_id=s.project_id"
				+ " where pj.org_id = ? and s.project_id=?"
				+ " group by s.site_id, s.site_name";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, projectId);
		return new JSONArray(result).toString();
	}

	public String getUnitWing(Unit unit) {
		int orgId = unit.getOrgId();
		int projectId = unit.getProjectId();
		int siteId = unit.getSiteId();

		String sql = "SELECT w.wing_id, w.wing_name FROM wing w"
				+ " inner join site s on w.project_id=s.project_id"
				+ " INNER JOIN project pj ON pj.org_id = s.org_id and s.project_id=pj.project_id"
				+ " where pj.org_id = ? and w.project_id=? and s.site_id=?"
				+ " group by w.wing_id, w.wing_name";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, projectId, siteId);
		return new JSONArray(result).toString();
	}

	public String getUnitFloor(Unit unit) {
		String sql = "SELECT u.unit_id, u.branch_id, u.created_date, u.floor_id, u.is_active, u.org_id, u.price, u.project_id, u.sr_no,"
				+ " u.totoal_sqrft, u.unit_name, u.updated_by, u.updated_date, u.user_id, u.wing_id, u.site_id,"
				+ " pj.project_name, w.wing_name, s.site_name, f.floor_name FROM unit u"
				+ " inner join floor f on u.floor_id=f.floor_id and u.wing_id=f.wing_id and u.site_id=f.site_id and u.org_id=f.org_id and u.project_id=f.project_id"
				+ " inner join wing w on u.wing_id=w.wing_id and u.site_id=w.site_id and u.org_id=w.org_id and u.project_id=w.project_id"
				+ " inner join site s on u.project_id=s.project_id and u.org_id=s.org_id"
				+ " INNER JOIN project pj ON pj.org_id = u.org_id and u.project_id=pj.project_id"
				+ " group by u.unit_id, u.branch_id, u.created_date, u.floor_id, u.is_active, u.org_id, u.price, u.project_id, u.sr_no,"
				+ " u.totoal_sqrft, u.unit_name, u.updated_by, u.updated_date, u.user_id, u.wing_id, u.site_id,"
				+ " pj.project_name, w.wing_name, s.site_name, f.floor_name order by u.unit_id desc";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		return new JSONArray(result).toString();
	}

	public Unit findUnitById(int Unit) {
		return unitRepo.findById(Unit).get();
	}

	public String deleteUnitByID(int Unit) {
		unitRepo.deleteById(Unit);
		return "deleted";
	}

}
