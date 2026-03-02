package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.schoolapp.entity.Admission;
import com.schoolapp.repository.AdmissionRepo;

@Component
public class AdmissionDao {

	@Autowired
	AdmissionRepo admissionRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	int sId = 1;

	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
		int admId = sId;
		try {
			byte[] imageData = file.getBytes();
			saveImageDataToDatabase(imageData, admId);
			return ResponseEntity.ok().body("Image uploaded successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
		}
	}

	public Object saveAdmission(Admission al) {
		Admission id = admissionRepo.save(al);
		sId = id.getAdmissionId();
		int pickUpId = id.getPickUpPoint();
		int classId = id.getClassId();
		int org_id = al.getOrgId();

		String sql = "insert into std_annual_fees(s_id, fees_structure_id, reasource_id, class_id, charges,"
				+ " finance_year, created_date, is_active, tr_date, user_id, branch_id, org_id, updated_by, updated_date)"
				+ " SELECT ad.admission_id, fs.fees_structure_id, fs.reasource_id, ad.class_id, fs.charges, fs.finance_year, ad.created_date"
				+ ", fs.is_active, ad.created_date, ad.user_id, ad.branch_id, ad.org_id, ad.updated_by, ad.updated_date"
				+ " FROM admission ad"
				+ " LEFT JOIN fees_structure fs ON fs.org_id=ad.org_id AND fs.class_id=ad.class_id"
				+ " WHERE ad.class_id = ? AND ad.admission_id = ? AND fs.org_id = ?";

		jdbcTemplate.update(sql, classId, sId, org_id);

		String sql1 = "insert into student_info(s_id, branch_id, created_date, current_address, father_name, father_occupation, home_contact, mobile_no,"
				+ " mother_name, mother_occupation, mothly_income, office_address, office_contact, org_id, permanent_address, updated_by, updated_date, user_id)"
				+ " SELECT admission_id, branch_id, created_date, '', middle_name, '', 0, 0,"
				+ " '', '', 0, '', 0, org_id, '', 0, updated_date, user_id FROM admission"
				+ " WHERE admission_id = ?";

		jdbcTemplate.update(sql1, sId);

		String sql2 = "insert into pre_school_info(admission_std, branch_id, created_date, date_of_admission, division, last_year_result, name_of_previous_school,"
				+ " org_id, school_id, std_of_previous_school, updated_by, updated_date, user_id, s_id)"
				+ " SELECT admission_id, branch_id, created_date, created_date, 0, '', '',"
				+ " org_id, 0, 0, 0, updated_date, user_id, admission_id"
				+ " FROM admission WHERE admission_id = ?";

		jdbcTemplate.update(sql2, sId);

		if (pickUpId > 0) {
			String sql3 = "SELECT bus_charges FROM bus_management WHERE bus_management_id = ?";
			Integer busCharges = jdbcTemplate.queryForObject(sql3, Integer.class, pickUpId);

			if (busCharges != null) {
				String sql4 = "SELECT sf.finance_year, sf.std_annual_fees_id, rm.reasource_id FROM reasource_master rm"
						+ " INNER JOIN std_annual_fees sf ON rm.org_id=sf.org_id AND rm.reasource_id=sf.reasource_id"
						+ " WHERE rm.reasource_name='Bus Fees' AND rm.org_id = ? AND sf.s_id = ?";

				List<Map<String, Object>> res = jdbcTemplate.queryForList(sql4, org_id, sId);
				if (!res.isEmpty()) {
					Map<String, Object> row = res.get(0);
					int finYear = (int) row.get("finance_year");
					int stdAnlFId = (int) row.get("std_annual_fees_id");
					int resId = (int) row.get("reasource_id");

					String sql5 = "UPDATE std_annual_fees SET charges = ? WHERE reasource_id = ?"
							+ " AND finance_year = ? AND s_id = ? AND std_annual_fees_id = ?";
					jdbcTemplate.update(sql5, busCharges, resId, finYear, sId, stdAnlFId);
				}
			}
		}

		return al;
	}

	private String saveImageDataToDatabase(byte[] imageData, int admId) {
		String sql = "UPDATE admission SET image_data = ? WHERE admission_id = ?";
		jdbcTemplate.update(sql, imageData, admId);
		return "done";
	}

	public String getAllAdmission(Admission admission) {
		int orgId = admission.getOrgId();
		String sql = "SELECT tcm.tc_std_name, tcm.taluka_name, tcm.dist_name, org.owner_contact, dist.dist_name, org.reg_id, c.city_name, tcm.conduct, tcm.date_of_leaving, tcm.reason_of_leaving, tcm.remark, org.org_name,"
				+ " org.invoice_address, org.reg_id, org.u_disk_id, cm.class_name, sti.current_address, psi.name_of_previous_school,"
				+ " ifnull(cs.cast_name, ' ') as cast_name, ifnull(scs.sub_cast_name, ' ') as sub_cast_name,"
				+ " ct.category_name, ad.admission_id, ad.age, ad.birth_place, ad.branch_id, ad.bus, ad.cast,"
				+ " ad.category, ad.class_id, ad.created_date, ad.dob, ad.first_name, ad.gender, ad.last_name, ad.middle_name, ad.mother_tongue,"
				+ " ad.nationality, ad.mother_name, ad.org_id, ad.religion, ad.sub_cast, ad.teacher_relative, ad.updated_by, ad.updated_date, ad.user_id,"
				+ " ad.class_standard, ad.grno, ad.adhar_no, ad.pick_up_point, ad.reg_id,"
				+ " ad.std_saral_portal_id, ad.u_disk_id, ad.rte, ad.mobile_no1, ad.mobile_no2"
				+ " FROM admission ad LEFT JOIN cast cs ON ad.cast=cs.cast_id"
				+ " LEFT JOIN pre_school_info psi ON ad.admission_id=psi.s_id"
				+ " LEFT JOIN sub_cast scs ON ad.sub_cast=scs.sub_cast_id"
				+ " LEFT JOIN student_info sti ON ad.admission_id=sti.s_id"
				+ " LEFT JOIN category ct ON ad.category=ct.category_id"
				+ " LEFT JOIN class_master cm ON ad.class_id=cm.class_id LEFT JOIN tc_master tcm ON ad.admission_id=tcm.tc_std_name"
				+ " INNER JOIN organization org ON ad.org_id=org.org_id LEFT JOIN district dist ON org.dist_id=dist.dist_id LEFT JOIN city c ON org.city_id=c.city_id WHERE ad.org_id = ?"
				+ " ORDER BY ad.admission_id DESC";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public String getAllStudentClassWise(Admission admission) {
		int orgId = admission.getOrgId();
		int classId = admission.getClassId();
		String sql = "SELECT admission_id, age, birth_place, branch_id, bus, cast, category, class_id, created_date, dob, first_name, gender, last_name, middle_name,"
				+ " mother_tongue, nationality, org_id, religion, sub_cast, teacher_relative, updated_by, updated_date, user_id"
				+ " FROM admission WHERE class_id = ? AND org_id = ?";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, classId, orgId);
		return new JSONArray(result).toString();
	}

	public String getPdfBonafied(Admission admission) {
		int orgId = admission.getOrgId();
		int sId = admission.getAdmissionId();
		String sql = "SELECT org.invoice_address, cm.class_name, ad.admission_id, org.org_name, DATE_FORMAT(ad.dob, '%d/%m/%Y') as dob, sf.finance_year, DATE_FORMAT(ad.created_date, '%d/%m/%Y') as admission_date,"
				+ " concat(ad.first_name, ' ', ad.middle_name, ' ', ad.last_name) as name, concat(ad.middle_name, ' ', ad.last_name) as father_name, ad.class_standard"
				+ " FROM admission ad"
				+ " INNER JOIN organization org ON ad.org_id=org.org_id"
				+ " INNER JOIN class_master cm ON ad.class_id=cm.class_id"
				+ " LEFT JOIN std_annual_fees sf ON ad.admission_id=sf.s_id"
				+ " WHERE ad.admission_id = ? AND ad.org_id = ?"
				+ " GROUP BY org.invoice_address, cm.class_name, ad.admission_id, org.org_name, ad.dob, sf.finance_year, ad.created_date, ad.first_name, ad.middle_name, ad.last_name, ad.class_standard";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, sId, orgId);
		return new JSONArray(result).toString();
	}

	public Admission findAdmissionById(int admissionID) {
		return admissionRepo.findById(admissionID).get();
	}

	public String updateAdmission(Admission admission) {
		int admissionId = admission.getAdmissionId();
		int age = admission.getAge();
		String birth_place = admission.getBirthPlace();
		String bus = admission.getBus();
		int cast = admission.getCast();
		int category = admission.getCategory();
		java.sql.Date dob = admission.getDob();
		String first_name = admission.getFirstName();
		String gender = admission.getGender();
		String last_name = admission.getLastName();
		String middle_name = admission.getMiddleName();
		String mother_tongue = admission.getMotherTongue();
		String nationality = admission.getNationality();
		String religion = admission.getReligion();
		int sub_cast = admission.getSubCast();
		String teacher_relative = admission.getTeacherRelative();
		String mobileNo1 = admission.getMobileNo1();
		String mobileNo2 = admission.getMobileNo2();
		String adharNo = admission.getAdharNo();

		String sql = "UPDATE admission SET age = ?, birth_place = ?, bus = ?, cast = ?, category = ?, dob = ?,"
				+ " first_name = ?, gender = ?, last_name = ?, middle_name = ?, mother_tongue = ?, nationality = ?,"
				+ " religion = ?, sub_cast = ?, teacher_relative = ?, mobile_no1 = ?, mobile_no2 = ?, adhar_no = ?"
				+ " WHERE admission_id = ?";

		jdbcTemplate.update(sql, age, birth_place, bus, cast, category, dob, first_name, gender, last_name, middle_name,
				mother_tongue, nationality, religion, sub_cast, teacher_relative, mobileNo1, mobileNo2, adharNo,
				admissionId);

		return "Record updated..!";
	}

	public String deleteAdmissionById(int admissionID) {
		admissionRepo.deleteById(admissionID);
		return "deleted";
	}

}
