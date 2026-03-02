package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.AccessPermission;
import com.schoolapp.entity.User;
import com.schoolapp.repository.UserRepo;

@Component
public class UserDao {

	@Autowired
	UserRepo userRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public User saveUser(User user) {
		System.out.println("Data inserted successfully...");
		return userRepo.save(user);
	}

	public String getAllUser(User users) {
		int orgId = users.getOrgId();
		String sql = "SELECT * FROM user WHERE org_id = ?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(rows).toString();
	}

	public AccessPermission userValidation(User users, AccessPermission accessPermission) {

		String email = users.getEmail();
		String pass = users.getPassword();
		int userId = users.getuId();

		String query;
		Object[] params;

		if (userId > 0) {
			query = "SELECT u.u_id, u.user_name, u.email, ap.access_create, ap.access_read, ap.access_delete, ap.access_update, u.user_sr_no, u.branch_id, u.org_id "
					+ "FROM access_permission ap INNER JOIN user u ON ap.access_user_id = u.u_id "
					+ "WHERE u.is_active = 1 AND u.user_id = ?";
			params = new Object[] { userId };
		} else {
			query = "SELECT u.u_id, u.user_name, u.email, ap.access_create, ap.access_read, ap.access_delete, ap.access_update, u.user_sr_no, u.branch_id, u.org_id "
					+ "FROM access_permission ap INNER JOIN user u ON ap.access_user_id = u.u_id "
					+ "WHERE email = ? AND password = ?";
			params = new Object[] { email, pass };
		}

		jdbcTemplate.query(query, rs -> {
			accessPermission.setAccessCreate(rs.getInt("access_create"));
			accessPermission.setAccessRead(rs.getInt("access_read"));
			accessPermission.setAccessDelete(rs.getInt("access_delete"));
			accessPermission.setAccessUpdate(rs.getInt("access_update"));

			users.setEmail(rs.getString("email"));
			users.setuId(rs.getInt("u_id"));
			users.setUserSrNo(rs.getInt("user_sr_no"));
			users.setUserName(rs.getString("user_name"));
			users.setBranchId(rs.getInt("branch_id"));
			users.setOrgId(rs.getInt("org_id"));
		}, params);

		return accessPermission;
	}

	public String valideUserDetailes(User users) {
		String email = users.getEmail();
		String pass = users.getPassword();

		String query = "SELECT * FROM user WHERE email = ? AND password = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query, email, pass);

		return new JSONArray(result).toString();
	}

	public String updateUser(User user) {

		Integer uId = user.getUserId();
		int userSrNo = user.getUserSrNo();
		String userName = user.getUserName();
		int branchId = user.getBranchId();
		int orgId = user.getOrgId();
		java.sql.Date date = user.getDate();
		String userContact = user.getUserContact();
		String panNo = user.getPanNo();
		String gstNo = user.getGstNo();
		String email = user.getEmail();
		String password = user.getPassword();
		String phone = user.getPhone();
		String uAddress = user.getuAddress();
		int income = user.getIncome();
		String incomeSource = user.getIncomeSource();
		int otherIncome = user.getOtherIncome();
		String otherIncomeSource = user.getOtherIncomeSource();
		String notes = user.getNotes();
		int isActive = user.getIsActive();
		int stateId = user.getStateId();
		int distId = user.getDistId();
		int cityId = user.getCityId();
		int userId = user.getUserId();
		java.sql.Date createdDate = user.getCreatedDate();
		int updatedBy = user.getUserId();
		java.sql.Date updatedDate = user.getUpdatedDate();

		String sql = "UPDATE user SET branch_id = ?, city_id = ?, created_date = ?, date = ?, dist_id = ?, email = ?, "
				+ "gst_no = ?, income = ?, income_source = ?, is_active = ?, notes = ?, other_income = ?, "
				+ "other_income_source = ?, pan_no = ?, phone = ?, state_id = ?, u_address = ?, updated_by = ?, updated_date = ?, "
				+ "user_contact = ?, user_id = ?, user_name = ?, user_sr_no = ?, password = ? "
				+ "WHERE u_id = ? AND org_id = ?";

		jdbcTemplate.update(sql, branchId, cityId, createdDate, date, distId, email, gstNo, income, incomeSource,
				isActive, notes, otherIncome, otherIncomeSource, panNo, phone, stateId, uAddress, updatedBy,
				updatedDate,
				userContact, userId, userName, userSrNo, password, uId, orgId);

		return "Record updated..!";
	}

	public String updateDeleteUser(User user) {
		Integer userId = user.getUserId();
		int updatedBy = user.getUpdatedBy();
		java.sql.Date updatedDate = user.getUpdatedDate();
		int orgId = user.getOrgId();
		int isActive = user.getIsActive();

		String sql = "UPDATE user SET is_active = ?, updated_by = ?, updated_date = ? WHERE u_id = ? AND org_id = ?";
		jdbcTemplate.update(sql, isActive, updatedBy, updatedDate, userId, orgId);

		return "Record updated..!";
	}

	public User findUserById(int UserId) {
		return userRepo.findById(UserId).get();
	}

	public String deleteUserById(int UserId) {
		userRepo.deleteById(UserId);
		return "deleted";
	}
}
