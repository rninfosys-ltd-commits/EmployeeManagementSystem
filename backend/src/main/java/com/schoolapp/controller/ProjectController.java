package com.schoolapp.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.Project;
import com.schoolapp.service.ProjectService;

@RestController
@RequestMapping("/project")

public class ProjectController {
	@Autowired
	ProjectService projectService;

	@PostMapping("/save")
	public String saveProject(@RequestBody Project project) throws ClassNotFoundException, SQLException {
		projectService.saveProject(project);
		return "Record save Successfully";
	}

	@GetMapping("/getAll")
	public List<Project> getAllUser() {
		return projectService.getAllProject();
	}

	@GetMapping("/get")
	public Project findProjectById(@RequestBody Project project) {

		return projectService.findProjectById(project.getProjectId());
		// return State;
	}

	@PutMapping("/updateDeleteProject")
	public String updateDeleteProject(@RequestBody Project project)
			throws ClassNotFoundException, SQLException {
		projectService.updateDeleteProject(project);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateProject(@RequestBody Project project) throws ClassNotFoundException, SQLException {
		projectService.updateProject(project);
		return "Record Updated..";
	}

	@DeleteMapping("/delete")
	public String deleteProjectById(@RequestBody Project project) {
		int id = project.getProjectId();

		if (id > 0) {
			projectService.deleteProjectById(id);
			return "deleted.." + id;
		}

		return "Wrong Id" + id;
	}

}
