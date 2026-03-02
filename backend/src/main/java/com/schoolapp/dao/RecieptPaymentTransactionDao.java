package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.RecieptPaymentTransaction;

@Component
public class RecieptPaymentTransactionDao {
	@Autowired
	com.schoolapp.repository.RecieptPaymentTransactionRepo RecieptPaymentTransactionRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public RecieptPaymentTransaction saveRecieptPaymentTransaction(
			RecieptPaymentTransaction recieptPaymentTransaction) {
		return RecieptPaymentTransactionRepo.save(recieptPaymentTransaction);
	}

	public String getAllRecieptPaymentTransaction(RecieptPaymentTransaction RecieptPaymentTransaction) {
		int orgId = RecieptPaymentTransaction.getOrgId();
		String sql = "SELECT p.org_id, p.cust_id, p.cust_name, p.phone, SUM(p.receipt) AS Balance "
				+ "FROM (select org_id, cust_id, cust_name, phone, 0 AS receipt "
				+ "from customer where org_id=? union "
				+ "SELECT cs.org_id, cs.cust_id, cs.cust_name, cs.phone, IFNULL(SUM(CASE WHEN rp.trn_type_id = 2 THEN rp.trn_amount * -1 ELSE 0 END), 0) AS receipt "
				+ "FROM reciept_payment_transaction rp " + "INNER JOIN organization org ON rp.org_id = org.org_id "
				+ "INNER JOIN user usr ON rp.user_id = usr.u_id "
				+ "INNER JOIN customer cs ON rp.customer_id = cs.cust_id WHERE cs.org_id = ? "
				+ "GROUP BY cs.org_id, cs.cust_id, cs.cust_name, cs.phone UNION "
				+ "SELECT cs.org_id, cs.cust_id, cs.cust_name, cs.phone, IFNULL(SUM(CASE WHEN rp.trn_type_id = 1 THEN rp.trn_amount ELSE 0 END), 0) AS receipt "
				+ "FROM reciept_payment_transaction rp " + "INNER JOIN organization org ON rp.org_id = org.org_id "
				+ "INNER JOIN user usr ON rp.user_id = usr.u_id "
				+ "INNER JOIN customer cs ON rp.customer_id = cs.cust_id WHERE cs.org_id = ? "
				+ "GROUP BY cs.org_id, cs.cust_id, cs.cust_name, cs.phone) p "
				+ "GROUP BY p.org_id, p.cust_id, p.cust_name, p.phone";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId, orgId, orgId);
		return new JSONArray(result).toString();
	}

	public String getAcStsmt(RecieptPaymentTransaction RecieptPaymentTransaction) {
		int orgId = RecieptPaymentTransaction.getOrgId();
		int custId = RecieptPaymentTransaction.getCustomerId();
		String sql = "SELECT subquery.created_date, subquery.trn_type_id, subquery.description, subquery.cheque_no, "
				+ "subquery.cheque_date, subquery.user_name, subquery.org_id, subquery.cust_id, subquery.cust_name, subquery.phone, subquery.Receipt, "
				+ "subquery.Payment, subquery.Receipt - subquery.Payment AS total FROM ( "
				+ "SELECT rp.created_date, rp.trn_type_id, rp.description, rp.cheque_no, rp.cheque_date, usr.user_name, cs.org_id, "
				+ "cs.cust_id, cs.cust_name, cs.phone, "
				+ "SUM(CASE WHEN rp.trn_type_id = 1 THEN rp.trn_amount ELSE 0 END) AS Receipt, "
				+ "SUM(CASE WHEN rp.trn_type_id = 2 THEN rp.trn_amount ELSE 0 END) AS Payment FROM "
				+ "reciept_payment_transaction rp INNER JOIN organization org ON rp.org_id = org.org_id "
				+ "INNER JOIN user usr ON rp.user_id = usr.u_id INNER JOIN "
				+ "customer cs ON rp.customer_id = cs.cust_id WHERE cs.cust_id=? and cs.org_id = ? "
				+ "GROUP BY rp.created_date, rp.trn_type_id, rp.description, rp.cheque_no, rp.cheque_date, usr.user_name, cs.org_id, cs.cust_id, cs.cust_name, cs.phone ) AS subquery";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, custId, orgId);
		return new JSONArray(result).toString();
	}

	public RecieptPaymentTransaction findRecieptPaymentTransactionById(int RecieptPaymentTransactionId) {
		return RecieptPaymentTransactionRepo.findById(RecieptPaymentTransactionId).get();
	}

	public String deleteRecieptPaymentTransactionById(int RecieptPaymentTransactionId) {
		RecieptPaymentTransactionRepo.deleteById(RecieptPaymentTransactionId);
		return "deleted";
	}
}
