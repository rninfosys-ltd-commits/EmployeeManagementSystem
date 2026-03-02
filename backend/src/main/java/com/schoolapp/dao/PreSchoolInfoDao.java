package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.jdbc.core.JdbcTemplate;
// import java.util.stream.Collectors;
// import java.util.stream.IntStream;

// import org.json.JSONArray;
// import org.json.JSONException;
// import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.PreSchoolInfo;
import com.schoolapp.repository.PreSchoolInfoRepo;

@Component
public class PreSchoolInfoDao {
	@Autowired
	PreSchoolInfoRepo preSchoolInfoRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public PreSchoolInfo savePreSchoolInfo(PreSchoolInfo preSchoolInfo) {

		return preSchoolInfoRepo.save(preSchoolInfo);
	}

	public String getAllPreSchoolInfo(PreSchoolInfo preSchoolInfo) {
		int orgId = preSchoolInfo.getOrgId();
		String sql = "SELECT * FROM pre_school_info where org_id=?";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public String updatePreSchoolInfo(PreSchoolInfo preSchoolInfo) {
		Integer srNo = preSchoolInfo.getPreSchoolInfoId();
		int admission_std = preSchoolInfo.getAdmissionStd();
		Date date_of_admission = preSchoolInfo.getDateOfAdmission();
		int division = preSchoolInfo.getDivision();
		String last_year_result = preSchoolInfo.getLastYearResult();
		String name_of_previous_school = preSchoolInfo.getNameOfPreviousSchool();
		int school_id = preSchoolInfo.getSchoolId();
		int std_of_previous_school = preSchoolInfo.getStdOfPreviousSchool();

		String query = "update pre_school_info set admission_std=?, date_of_admission=?, division=?, last_year_result=?, name_of_previous_school=?, "
				+ " school_id=?, std_of_previous_school=? where pre_school_info_id=?";

		jdbcTemplate.update(query, admission_std, date_of_admission, division, last_year_result,
				name_of_previous_school,
				school_id, std_of_previous_school, srNo);

		System.out.println("Record updated successfully..!");
		return "Record updated..!";
	}

	public PreSchoolInfo findPreSchoolInfoById(int preSchoolInfoID) {
		return preSchoolInfoRepo.findById(preSchoolInfoID).get();
	}

	public String deletePreSchoolInfoById(int preSchoolInfoID) {
		preSchoolInfoRepo.deleteById(preSchoolInfoID);
		;
		return "deleted";
	}

}
