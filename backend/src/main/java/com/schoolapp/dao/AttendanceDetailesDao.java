package com.schoolapp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.AttendanceDetailes;

@Component
public class AttendanceDetailesDao {
	@Autowired
	com.schoolapp.repository.AttendanceDetailesRepo AttendanceDetailesRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ArrayList<AttendanceDetailes> saveAttendanceDetailes(ArrayList<AttendanceDetailes> attendance) {
		ArrayList<AttendanceDetailes> path = null;
		for (AttendanceDetailes fee : attendance) {
			int teaId = fee.getTeacherId();
			String columnName = "d" + teaId;

			int id = switch (teaId) {
				case 1 -> fee.getD1();
				case 2 -> fee.getD2();
				case 3 -> fee.getD3();
				case 4 -> fee.getD4();
				case 5 -> fee.getD5();
				default -> 0;
			};

			String countSql = "SELECT attendance_id FROM attendance_detailes where s_id=? and subject_id=? and year=? and month=? limit 1";
			List<Map<String, Object>> existing = jdbcTemplate.queryForList(countSql, fee.getSId(), fee.getSubjectId(),
					fee.getYear(), fee.getMonth());

			if (existing.isEmpty()) {
				path = (ArrayList<AttendanceDetailes>) AttendanceDetailesRepo.saveAll(attendance);
				System.out.println("Attendance Saved..!");
			} else {
				int attendanceId = (int) existing.get(0).get("attendance_id");
				String updateSql = "UPDATE attendance_detailes SET " + columnName + " = ? WHERE attendance_id = ?";
				jdbcTemplate.update(updateSql, id, attendanceId);
				System.out.println("Attendance updated for " + columnName);
			}
		}
		return path;
	}

	public String getAllAttendanceDetailes(AttendanceDetailes AttendanceDetailes) {
		int orgId = AttendanceDetailes.getOrgId();
		String sql = "SELECT concat(ad.first_name,' ',ad.middle_name,' ',ad.last_name) as name, atd.attendance_id, atd.class_id, atd.d1, atd.div_id, atd.month, atd.s_id, atd.subject_id, atd.teacher_id, atd.time, atd.year, atd.branch_id, atd.created_date, atd.d10, atd.d11, atd.d12, atd.d13, atd.d14, atd.d15, atd.d16, atd.d17, atd.d18, atd.d19, atd.d2, atd.d20, atd.d21, atd.d22, atd.d23, atd.d24, atd.d25, atd.d26, atd.d27, atd.d28, atd.d29, atd.d3, atd.d30, atd.d31, atd.d4, d5, atd.d6, atd.d7, atd.d8, atd.d9, atd.is_active, atd.org_id, atd.updated_by, atd.updated_date, atd.user_id "
				+ "FROM attendance_detailes atd " + "inner join admission ad on atd.s_id=ad.admission_id "
				+ "where atd.org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public AttendanceDetailes updateDeleteAttendanceDetailes(AttendanceDetailes AttendanceDetailes) {
		String sql = "update attendance_detailes set is_active=?, updated_by=?, updated_date=? "
				+ "where attendance_id=? and org_id=?";
		jdbcTemplate.update(sql, AttendanceDetailes.getIsActive(), AttendanceDetailes.getUpdatedBy(),
				AttendanceDetailes.getUpdatedDate(), AttendanceDetailes.getAttendanceId(),
				AttendanceDetailes.getOrgId());
		System.out.println("Record updated");
		return AttendanceDetailesRepo.save(AttendanceDetailes);
	}

	public AttendanceDetailes findAttendanceDetailesById(int AttendanceDetailesId) {
		return (AttendanceDetailes) AttendanceDetailesRepo.findById(AttendanceDetailesId).get();
	}

	public String deleteAttendanceDetailesById(int AttendanceDetailesId) {
		AttendanceDetailesRepo.deleteById(AttendanceDetailesId);
		return "deleted";
	}
}
