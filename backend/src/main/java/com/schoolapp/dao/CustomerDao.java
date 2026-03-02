package com.schoolapp.dao;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Customer;
import com.schoolapp.repository.CustomerRepo;

@Component
public class CustomerDao {

	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Customer saveCustomer(Customer customer) {
		System.out.println("Data inserted successfully...");
		return customerRepo.save(customer);
	}

	public Customer updateDeleteCustomer(Customer customer) {
		Integer customerId = customer.getCustId();
		int updatedBy = customer.getUpdatedBy();
		java.util.Date updatedDate = customer.getUpdatedDate();
		int isActive = customer.getIsActive();

		String sql = "UPDATE customer SET is_active = ?, updated_by = ?, updated_date = ? WHERE cust_id = ?";
		jdbcTemplate.update(sql, isActive, updatedBy, updatedDate, customerId);

		System.out.println("Record updated");
		return customerRepo.save(customer);
	}

	public String getAllCustomer(Customer customer) {
		int orgId = customer.getOrgId();
		String sql = "SELECT * FROM customer WHERE org_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, orgId);
		return new JSONArray(result).toString();
	}

	public Customer findCustomerById(int CustomerId) {
		return customerRepo.findById(CustomerId).get();
	}

	public String deleteCustomerById(int CustomerId) {
		customerRepo.deleteById(CustomerId);
		return "deleted";
	}
}
