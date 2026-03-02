package com.schoolapp.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.User;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.dao.RegisterRequest;
import com.schoolapp.service.UserService;
import com.schoolapp.service.UsersService;

@RestController
@RequestMapping("/api/users")

public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UsersService usersService;

	@PostMapping("/save")
	public String saveUser(@RequestBody User users) throws ClassNotFoundException, SQLException {
		userService.saveUser(users);
		return "Record save successfully";
	}

	@PostMapping("/getAll")
	public String getAllUser(@RequestBody User users) throws ClassNotFoundException, SQLException {
		return userService.getAllUser(users);
	}

	@PostMapping("/postUser")
	public String valideUserDetailes(@RequestBody User users) throws ClassNotFoundException, SQLException {
		return userService.valideUserDetailes(users);
	}

	@GetMapping("/get")
	public User findUserById(@RequestBody User users) {

		return userService.findUserById(users.getuId());
		// return State;
	}

	@PutMapping("/update")
	public String updateUser(@RequestBody User user) throws ClassNotFoundException, SQLException {
		userService.updateUser(user);
		return "Record Updated.....";
	}

	@PutMapping("/updateDeleteUser")
	public String updateDeleteUser(@RequestBody User user) throws ClassNotFoundException, SQLException {
		userService.updateDeleteUser(user);
		return "Record Updated.....";
	}

	@GetMapping("/parties")
	public List<UserEntity> getParties() {
		return userService.getParties();
	}

	@GetMapping("/all")
	public List<UserEntity> getAllUsers() throws Exception {
		return usersService.getAllUsers();
	}

	@GetMapping("")
	public List<UserEntity> getAllUsersRoot() throws Exception {
		return usersService.getAllUsers();
	}

	@PostMapping("")
	public void createUser(@RequestBody RegisterRequest request) {
		usersService.register(request);
	}

	@GetMapping("/{id}")
	public UserEntity getUserById(@PathVariable Long id) {
		return usersService.getUser(id);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		usersService.deleteUser(id);
	}

	@PutMapping("/{id}")
	public UserEntity updateUserById(@PathVariable Long id, @RequestBody UserEntity userDetails) {
		return usersService.updateUser(id, userDetails);
	}

	@PutMapping("/{id}/activate")
	public void activateUser(@PathVariable Long id) {
		usersService.updateUserStatus(id, true);
	}

	@PutMapping("/{id}/deactivate")
	public void deactivateUser(@PathVariable Long id) {
		usersService.updateUserStatus(id, false);
	}

	@DeleteMapping("/delete")
	public String deleteUserById(@RequestBody User users) {
		int id = users.getuId();

		if (id > 0) {
			userService.deleteUserById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}

}
