package com.schoolapp.controller;

import java.sql.SQLException;
// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.Customer;
import com.schoolapp.service.CustomerService;

@RestController
@RequestMapping("/customer")

public class CustomerController {

	@Autowired
	CustomerService customerService;

	@PostMapping("/save")
	public String saveCustomer(@RequestBody Customer customer) throws ClassNotFoundException, SQLException {
		customerService.saveCustomer(customer);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllCustomers(@RequestBody Customer customer) throws ClassNotFoundException, SQLException {
		return customerService.getAllCustomer(customer);
	}

	@GetMapping("/get")
	public Customer findCustomerById(@RequestBody Customer customer) {

		return customerService.findCustomerById(customer.getCustId());
		// return State;
	}

	@PutMapping("/updateDeleteCustomer")
	public String updateDeleteCustomer(@RequestBody Customer customer) throws ClassNotFoundException, SQLException {

		customerService.updateDeleteCustomer(customer);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateCustomer(@RequestBody Customer customer) throws ClassNotFoundException, SQLException {
		customerService.updateCustomer(customer);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteCustomerById(@RequestBody Customer customer) {
		int id = customer.getCustId();

		if (id > 0) {
			customerService.deleteCustomerById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}
