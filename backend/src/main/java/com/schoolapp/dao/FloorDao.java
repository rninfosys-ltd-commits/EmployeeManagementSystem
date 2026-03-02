package com.schoolapp.dao;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Floor;
import com.schoolapp.repository.FloorRepo;

@Component
public class FloorDao {
	@Autowired
	FloorRepo floorRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Floor saveFloor(Floor floor) {
		String checkSql = "select count(1) from floor where project_id=? and site_id=? and wing_id=? and org_id=? and floor_name=?";
		Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, floor.getProjectId(), floor.getSiteId(),
				floor.getWingId(), floor.getOrgId(), floor.getFloorName());

		if (count == null || count == 0) {
			Floor path = floorRepo.save(floor);

			String productSql = "insert into product (name, br_id, brand_id, category, categoryid, cgst, created_date, cust_id, d1, d2, hsncode, igst, is_active, itm_type, org_id, productdiscription, purchase_rate, quantity, rate, sgst, shrtnm, unit, updated_by, updated_date, user_id, wrates) "
					+ "SELECT concat(pj.project_name,' - ',s.site_name,' - ',w.wing_name,' - ',f.floor_name), 1, 0, 0, 0, 0, '2024-02-23', 1, 0, 0, 0, 0, 1, 0, 1, '', 0, 0, 0, 0, 0, 0, 0, '2024-02-23', 1, 0 "
					+ "FROM project pj " + "inner join site s on pj.org_id=s.org_id and pj.project_id=s.site_id "
					+ "inner join wing w on s.org_id=w.org_id and s.site_id=w.site_id and s.project_id=w.project_id "
					+ "inner join floor f on w.org_id=f.org_id and f.project_id=w.project_id and w.wing_id=f.wing_id "
					+ "where pj.project_id=? and s.site_id=? and w.wing_id=? and f.org_id=? and f.floor_id=?";

			jdbcTemplate.update(productSql, floor.getProjectId(), floor.getSiteId(), floor.getWingId(),
					floor.getOrgId(), path.getFloorId());

			System.out.println("product save successfully..!");
			return path;
		} else {
			System.out.println("floor already exist");
			return null;
		}
	}

	public String updateFloor(Floor floor) {
		String query = "update floor set branch_id=?, created_date=?, floor_name=?, is_active=?, project_id=?, srno=?, updated_by=?, updated_date=?, wing_id=? "
				+ "where floor_id=? and org_id=?";
		jdbcTemplate.update(query, floor.getBranchId(), floor.getCreatedDate(), floor.getFloorName(),
				floor.getIsActive(), floor.getProjectId(), floor.getSrno(), floor.getUserId(), floor.getUpdatedDate(),
				floor.getWingId(), floor.getFloorId(), floor.getOrgId());
		System.out.println("Record updated successfully..!");
		return "Record updated..!";
	}

	public Floor updateDeleteFloor(Floor floor) {
		String query = "update floor set is_active=?, updated_by=?, updated_date=? "
				+ "where floor_id=? and org_id=?";
		jdbcTemplate.update(query, floor.getIsActive(), floor.getUpdatedBy(), floor.getUpdatedDate(),
				floor.getFloorId(), floor.getOrgId());
		System.out.println("Record updated");
		return floorRepo.save(floor);
	}

	public String getAllFloor() {
		String sql = "SELECT f.*, p.project_name, w.wing_name, s.site_name " + "FROM floor f "
				+ "inner join wing w on f.wing_id=w.wing_id and f.project_id=w.project_id and f.org_id=w.org_id "
				+ "inner join site s on f.site_id=s.site_id and f.org_id=s.org_id "
				+ "inner join project p on f.project_id=p.project_id and f.org_id=p.org_id";
		return new JSONArray(jdbcTemplate.queryForList(sql)).toString();
	}

	public String getFloorSite(Floor floor) {
		String sql = "SELECT s.site_id, s.site_name FROM site s "
				+ "INNER JOIN project pj ON pj.org_id = s.org_id and pj.project_id=s.project_id "
				+ "where pj.org_id = ? and s.project_id=? " + "group by s.site_id";
		return new JSONArray(jdbcTemplate.queryForList(sql, floor.getOrgId(), floor.getProjectId())).toString();
	}

	public String getFloorWing(Floor floor) {
		String sql = "SELECT w.wing_id, w.wing_name FROM wing w " + "inner join site s on w.project_id=s.project_id "
				+ "INNER JOIN project pj ON pj.org_id = s.org_id and s.project_id=pj.project_id "
				+ "where pj.org_id = ? and w.project_id=? and s.site_id=? " + "group by w.wing_id, w.wing_name";
		return new JSONArray(jdbcTemplate.queryForList(sql, floor.getOrgId(), floor.getProjectId(), floor.getSiteId()))
				.toString();
	}

	public Floor findFloorById(int floor) {
		return floorRepo.findById(floor).get();
	}

	public String deleteFloorByID(int floor) {
		floorRepo.deleteById(floor);
		return "deleted";
	}

}
