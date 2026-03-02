package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Organization;
import com.schoolapp.repository.OrganizationRepo;

@Component
public class OrganizationDao {

	@Autowired
	OrganizationRepo organizationRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Organization saveOrganization(Organization organization) {

		Organization path = null;
		String emailId = organization.getEmail();

		String query = "SELECT count(1) FROM user where email=?";
		Integer cnt = jdbcTemplate.queryForObject(query, Integer.class, emailId);

		if (cnt != null && cnt >= 0) {
			path = organizationRepo.save(organization);

			System.out.println("Save orgnisation records...");

			int orgId = path.getOrgId();

			int city_id = organization.getCityId();
			Date created_date = organization.getCreatedDate();
			Date date = organization.getDate();
			int dist_id = organization.getDistId();
			String gst_no = organization.getGstNo();
			int income = organization.getIncome();
			String income_source = organization.getIncomeSource();
			String notes = organization.getNotes();
			int other_income = organization.getOtherIncome();
			String other_income_source = organization.getOtherIncomeSource();
			String pan_no = organization.getPanNo();
			String phone = organization.getPhone();
			int state_id = organization.getStateId();
			String u_address = organization.getInvoiceAddress();
			int updated_by = organization.getUpdatedBy();
			Date updated_date = organization.getUpdatedDate();
			String user_contact = organization.getPhone();
			String user_name = organization.getOrgName();

			int branch_id = organization.getOrgId();
			int user_id = organization.getOrgId();

			String sql1 = "INSERT INTO user(city_id, created_date, date, dist_id, email, gst_no, income, income_source, "
					+ "is_active, notes, org_id,other_income,other_income_source, pan_no, password, phone, state_id, u_address, "
					+ "updated_by, updated_date, user_contact, user_id, user_name,user_sr_no,branch_id) "
					+ "values (?, ?, ?, ?,?,?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?,?)";

			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement ps1 = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
				ps1.setInt(1, city_id);
				ps1.setDate(2, created_date);
				ps1.setDate(3, date);
				ps1.setInt(4, dist_id);
				ps1.setString(5, emailId);
				ps1.setString(6, gst_no);
				ps1.setInt(7, income);
				ps1.setString(8, income_source);
				ps1.setInt(9, 1);
				ps1.setString(10, notes);
				ps1.setInt(11, orgId);
				ps1.setInt(12, other_income);
				ps1.setString(13, other_income_source);
				ps1.setString(14, pan_no);
				ps1.setString(15, "111");
				ps1.setString(16, phone);
				ps1.setInt(17, state_id);
				ps1.setString(18, u_address);
				ps1.setInt(19, updated_by);
				ps1.setDate(20, updated_date);
				ps1.setString(21, user_contact);
				ps1.setInt(22, user_id);
				ps1.setString(23, user_name);
				ps1.setInt(24, 0);
				ps1.setInt(25, branch_id);
				return ps1;
			}, keyHolder);

			int generatedUserId = keyHolder.getKey().intValue();

			String sql3 = "UPDATE user SET user_id = ? WHERE org_id = ?";
			jdbcTemplate.update(sql3, generatedUserId, orgId);

			String sql2 = "INSERT INTO access_permission(access_create, access_delete, access_read, access_update, access_user_id, branch_id, "
					+ "created_date, is_active, model_id, org_id, updated_by, updated_date, user_id) "
					+ "values (?, ?, ?, ?,?,?, ?, ?, ?, ?,?,?,?)";

			jdbcTemplate.update(sql2, 1, 1, 1, 1, generatedUserId, branch_id, created_date, 1, 1, orgId, updated_by,
					updated_date, generatedUserId);

			System.out.println("DOneeee");
		} else {
			System.out.println("already present");
		}
		return path;
	}

	public Organization updateDeleteOrganization(Organization organization) {
		Integer orgId = organization.getOrgId();
		int updatedBy = organization.getUpdatedBy();
		Date updatedDate = organization.getUpdatedDate();
		int isActive = organization.getIsActive();

		String query = "update organization set is_active=?, updated_by=?,updated_date=? where org_id=?";
		jdbcTemplate.update(query, isActive, updatedBy, updatedDate, orgId);
		System.out.println("Record updated");
		return organizationRepo.save(organization);
	}

	public List<Organization> getAllOrganizations() {
		return organizationRepo.findAll();
	}

	public Organization findOrganizationById(int organizationId) {
		return organizationRepo.findById(organizationId).get();
	}

	public String deleteOrgnizationById(int OrgId) {
		organizationRepo.deleteById(OrgId);
		return "deleted";
	}

}
