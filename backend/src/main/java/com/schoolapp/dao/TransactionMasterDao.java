package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.TransactionMaster;
import com.schoolapp.repository.TransactionMasterRepo;

@Component
public class TransactionMasterDao {

	@Autowired
	TransactionMasterRepo TransactionMasterRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public TransactionMaster saveTransactionMaster(TransactionMaster TransactionMaster) {
		return TransactionMasterRepo.save(TransactionMaster);
	}

	public String getAllTransactionMaster(TransactionMaster TransactionMaster) {
		int trMstId = TransactionMaster.getTransactionMasterId();
		String sql = "SELECT usr.user_name, org.org_name, org.invoice_address, rm.reasource_name, std.recived_amt, cm.class_name, concat(ad.first_name,\" \",ad.middle_name,\" \",ad.last_name) as name, tm.transaction_master_id, "
				+ "tm.branch_id, tm.created_date, tm.description, tm.finance_year, tm.is_active, tm.org_id, tm.recived_amt, tm.s_id, tm.sr_no, tm.tr_date, tm.updated_by, "
				+ "tm.updated_date, tm.user_id " + "FROM transaction_master tm "
				+ "inner join admission ad on tm.s_id=ad.admission_id "
				+ "inner join class_master cm on ad.class_id=cm.class_id "
				+ "inner join std_annual_fees sf on ad.admission_id=sf.s_id "
				+ "left join std_transaction_detailes std on sf.std_annual_fees_id=std.std_annual_fees_id and tm.transaction_master_id=std.payment_rcv_id "
				+ "inner join reasource_master rm on sf.reasource_id=rm.reasource_id "
				+ "inner join organization org on tm.org_id=org.org_id "
				+ "inner join user usr on tm.user_id=usr.u_id "
				+ "where tm.transaction_master_id=?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, trMstId);
		return new JSONArray(result).toString();
	}

	public TransactionMaster findTransactionMasterById(int TransactionMasterId) {
		return TransactionMasterRepo.findById(TransactionMasterId).get();
	}

	public String deleteTransactionMasterById(int TransactionMasterId) {
		TransactionMasterRepo.deleteById(TransactionMasterId);
		return "deleted";
	}

}
