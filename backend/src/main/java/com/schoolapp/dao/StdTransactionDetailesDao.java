package com.schoolapp.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.StdAnnualFees;
import com.schoolapp.entity.StdTransactionDetailes;
// import com.schoolapp.entity.TransactionMaster;
import com.schoolapp.repository.StdTransactionDetailesRepo;

@Component
public class StdTransactionDetailesDao {
	@Autowired
	StdTransactionDetailesRepo stdTransactionDetailesRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<StdTransactionDetailes> saveStdTransactionDetailes(
			List<StdTransactionDetailes> stdTransactionDetailes) {

		int transactionMasterId = 0;
		String description = null;
		int recivedAmt = 0;
		Date trDate = null;
		int userId = 0;
		int orgId = 0;
		int branchId = 0;
		Date createdDate = null;
		int updatedBy = 0;
		Date updatedDate = null;
		int cnt = 0;
		int sumRcvAmt = 0;
		int stdAnlFessId = 0;
		int sId = 0;
		int financeYear = 0;

		for (StdTransactionDetailes al2 : stdTransactionDetailes) {
			stdAnlFessId = al2.getStdAnnualFeesId();
		}
		System.out.println("anuall id : " + stdAnlFessId);

		String sqlFinance = "SELECT finance_year, s_id FROM std_annual_fees where std_annual_fees_id=?";
		Map<String, Object> financeResult = jdbcTemplate.queryForMap(sqlFinance, stdAnlFessId);
		if (financeResult != null) {
			financeYear = (Integer) financeResult.get("finance_year");
			sId = (Integer) financeResult.get("s_id");
		}

		for (StdTransactionDetailes al1 : stdTransactionDetailes) {
			recivedAmt = al1.getRecivedAmt();
			sumRcvAmt += recivedAmt;
		}

		for (StdTransactionDetailes al : stdTransactionDetailes) {
			description = al.getDescription();
			branchId = al.getBranchId();
			userId = al.getUserId();
			orgId = al.getOrgId();
			createdDate = al.getCreatedDate();
			trDate = al.getCreatedDate();
			updatedBy = al.getUpdatedBy();
			updatedDate = al.getUpdatedDate();

			if (cnt == 0) {
				cnt = 1;
				String sqlInsert = "INSERT INTO transaction_master (branch_id, created_date, description, finance_year,is_active, org_id, recived_amt, s_id, sr_no, "
						+ "tr_date, updated_by, updated_date, user_id )  VALUES (?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?,?)";

				KeyHolder keyHolder = new GeneratedKeyHolder();
				final int fFinanceYear = financeYear;
				final int fSId = sId;
				final int fSumRcvAmt = sumRcvAmt;
				final String fDescription = description;
				final int fBranchId = branchId;
				final int fOrgId = orgId;
				final int fUserId = userId;
				final Date fCreatedDate = createdDate;
				final Date fTrDate = trDate;
				final int fUpdatedBy = updatedBy;
				final Date fUpdatedDate = updatedDate;

				jdbcTemplate.update(connection -> {
					PreparedStatement pst = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
					pst.setInt(1, fBranchId);
					pst.setDate(2, fCreatedDate);
					pst.setString(3, fDescription);
					pst.setInt(4, fFinanceYear);
					pst.setInt(5, 1);
					pst.setInt(6, fOrgId);
					pst.setInt(7, fSumRcvAmt);
					pst.setInt(8, fSId);
					pst.setInt(9, 0); // srNo
					pst.setDate(10, fTrDate);
					pst.setInt(11, fUpdatedBy);
					pst.setDate(12, fUpdatedDate);
					pst.setInt(13, fUserId);
					return pst;
				}, keyHolder);

				transactionMasterId = keyHolder.getKey().intValue();
			}
			al.setPaymentRcvId(transactionMasterId);
		}

		return stdTransactionDetailesRepo.saveAll(stdTransactionDetailes);
	}

	public String getAllStdTransactionDetailes(StdTransactionDetailes StdTransactionDetailes) {
		int orgId = StdTransactionDetailes.getOrgId();
		String sql = "SELECT std_transaction_detailes_id, branch_id, cheque_date, cheque_no, created_date, description, org_id, payment_rcv_id, "
				+ "pre_balance, recived_amt, std_annual_fees_id, tr_date, updated_by, updated_date, user_id "
				+ "FROM schooldb.std_transaction_detailes where org_id=?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public String stdYearwiseFeesTrnDetailes(StdAnnualFees StdAnnualFees) {
		int financialYear = StdAnnualFees.getFinanceYear();
		int sId = StdAnnualFees.getSid();
		String sql = "SELECT sf.std_annual_fees_id, ad.admission_id, rm.reasource_name, sf.charges, ifnull(sum(tr.recived_amt),0) as rcv_amount, count(1) "
				+ "FROM std_annual_fees sf " + "inner join admission ad on sf.s_id=ad.admission_id "
				+ "inner join reasource_master rm on sf.reasource_id=rm.reasource_id "
				+ "left join std_transaction_detailes tr on sf.std_annual_fees_id=tr.std_annual_fees_id "
				+ "where sf.finance_year = ? and sf.s_id=? "
				+ "group by sf.std_annual_fees_id, ad.admission_id, rm.reasource_name, sf.charges";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, financialYear, sId);
		return new JSONArray(result).toString();
	}

	public String getAllTranMst(StdTransactionDetailes StdTransactionDetailes) {
		int orgId = StdTransactionDetailes.getOrgId();
		String sql = "SELECT cm.class_name, concat(ad.first_name,\" \",ad.middle_name,\" \",ad.last_name) as name, tm.transaction_master_id, tm.branch_id, tm.created_date, tm.description, tm.finance_year, tm.is_active, tm.org_id, tm.recived_amt, tm.s_id, "
				+ "tm.sr_no, tm.tr_date, tm.updated_by, tm.updated_date, tm.user_id "
				+ "FROM transaction_master tm " + "inner join admission ad on tm.s_id=ad.admission_id "
				+ "inner join class_master cm on ad.class_id=cm.class_id " + "where tm.org_id=?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public String getPdfRcp(StdTransactionDetailes StdTransactionDetailes) {
		int trMstId = StdTransactionDetailes.getPaymentRcvId();
		String sql = "SELECT usr.user_name, org.org_name, org.invoice_address, rm.reasource_name, std.recived_amt, cm.class_name, concat(ad.first_name,\" \",ad.middle_name,\" \",ad.last_name) as name, tm.transaction_master_id, "
				+ "tm.branch_id, tm.created_date, tm.description, tm.finance_year, tm.is_active, tm.org_id, tm.recived_amt, tm.s_id, tm.sr_no, DATE_FORMAT(tm.tr_date,'%d/%m/%Y') as tr_date, tm.updated_by, "
				+ "tm.updated_date, tm.user_id " + "FROM transaction_master tm "
				+ "inner join admission ad on tm.s_id=ad.admission_id "
				+ "inner join class_master cm on ad.class_id=cm.class_id "
				+ "inner join std_annual_fees sf on ad.admission_id=sf.s_id "
				+ "left join std_transaction_detailes std on sf.std_annual_fees_id=std.std_annual_fees_id and tm.transaction_master_id=std.payment_rcv_id "
				+ "inner join reasource_master rm on sf.reasource_id=rm.reasource_id "
				+ "inner join organization org on tm.org_id=org.org_id "
				+ "inner join user usr on tm.user_id=usr.u_id "
				+ "where std.recived_amt>0 and tm.transaction_master_id=?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, trMstId);
		return new JSONArray(result).toString();
	}

	public StdTransactionDetailes findStdTransactionDetailesById(int stdTransactionDetailesId) {
		return stdTransactionDetailesRepo.findById(stdTransactionDetailesId).get();
	}

	public String deleteStdTransactionDetailesById(int stdTransactionDetailesId) {
		stdTransactionDetailesRepo.deleteById(stdTransactionDetailesId);
		return "deleted";
	}
}
