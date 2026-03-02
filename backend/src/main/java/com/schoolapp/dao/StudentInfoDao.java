package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.StudentInfo;
import com.schoolapp.repository.StudentInfoRepo;

@Component
public class StudentInfoDao {

	@Autowired
	StudentInfoRepo studentInfoRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public StudentInfo saveStudentInfo(StudentInfo studentInfo) {

		return studentInfoRepo.save(studentInfo);
	}

	public String getAllStudentInfo(StudentInfo studentInfo) {
		int orgId = studentInfo.getOrgId();
		String sql = "SELECT student_info_id, current_address, father_name, father_occupation, home_contact, mobile_no, mother_name, mother_occupation, mothly_income, office_address, office_contact, permanent_address, branch_id, created_date, org_id, updated_by, updated_date, user_id, s_id FROM student_info where org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public StudentInfo findStudentInfoById(int StudentInfoID) {
		return studentInfoRepo.findById(StudentInfoID).get();
	}

	public String updateStudentInfo(StudentInfo studentInfo) {
		Integer studentId = studentInfo.getStudentInfoId();
		String currentAddress = studentInfo.getCurrentAddress();
		String fatherName = studentInfo.getFatherName();
		String fatherOccupation = studentInfo.getFatherOccupation();
		int homeContact = studentInfo.getHomeContact();
		int mobileNo = studentInfo.getMobileNo();
		String motherName = studentInfo.getMotherName();
		String motherOccupation = studentInfo.getMotherOccupation();
		int mothlyIncome = studentInfo.getMothlyIncome();
		String officeAddress = studentInfo.getOfficeAddress();
		int officeContact = studentInfo.getOfficeContact();
		String permanentAddress = studentInfo.getPermanentAddress();

		String sql = "UPDATE student_info SET current_address = ?, father_name = ?, father_occupation = ?, home_contact = ?, mobile_no = ?,"
				+ " mother_name = ?, mother_occupation = ?, mothly_income = ?, office_address = ?, office_contact = ?, permanent_address = ? WHERE student_info_id = ?";

		jdbcTemplate.update(sql, currentAddress, fatherName, fatherOccupation, homeContact, mobileNo, motherName,
				motherOccupation, mothlyIncome, officeAddress, officeContact, permanentAddress, studentId);

		return "Record updated..!";
	}

	public String deleteStudentInfoById(int studentInfoID) {
		studentInfoRepo.deleteById(studentInfoID);
		return "deleted";
	}
}
