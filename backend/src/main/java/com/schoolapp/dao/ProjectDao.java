package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Project;
import com.schoolapp.repository.ProjectRepo;

@Component
public class ProjectDao {
	@Autowired
	ProjectRepo projectRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Project saveProject(Project project) {
		System.out.println("Data inserted successfully...");
		return projectRepo.save(project);

	}

	public String updateProject(Project project) {
		String query = "update project set address = ? , branch_id = ? , budget_amt = ? , builder_name = ? , created_date = ? ,"
				+ " end_date = ? , is_active = ? , land_owner = ?  , previous_land_owner = ? , project_name = ? , rera_no = ? ,"
				+ " sanction_date = ? , sr_no = ? , srv_gut_no = ? , start_date = ? , updated_by = ? , updated_date = ? , user_id = ?"
				+ " where project_id= ? and org_id = ?";

		jdbcTemplate.update(query, project.getAddress(), project.getBranchId(), project.getBudgetAmt(),
				project.getBuilderName(), project.getCreatedDate(), project.getEndDate(), project.getIsActive(),
				project.getLandOwner(), project.getPreviousLandOwner(), project.getProjectName(), project.getReraNo(),
				project.getSanctionDate(), project.getSrNo(), project.getSrvGutNo(), project.getStartDate(),
				project.getUserId(), project.getUpdatedDate(), project.getUserId(), project.getProjectId(),
				project.getOrgId());

		System.out.println("Record updated successfully..!");
		return "Record updated..!";
	}

	public Project updateDeleteProject(Project project) {
		String query = "update project set is_active=?, updated_by=?, updated_date=? "
				+ "where project_id=? and org_id=?";
		jdbcTemplate.update(query, project.getIsActive(), project.getUpdatedBy(), project.getUpdatedDate(),
				project.getProjectId(), project.getOrgId());
		System.out.println("Record updated");
		return projectRepo.save(project);
	}

	public List<Project> getAllProject() {
		return projectRepo.findAll();
	}

	public Project findProjectById(int project) {
		return projectRepo.findById(project).get();
	}

	public String deleteUserById(int project) {
		projectRepo.deleteById(project);
		return "deleted";
	}
}
