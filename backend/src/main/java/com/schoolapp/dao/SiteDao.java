package com.schoolapp.dao;

import java.sql.Date;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Site;
import com.schoolapp.repository.SiteRepo;

@Component
public class SiteDao {
	@Autowired
	SiteRepo siteRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Site saveSite(Site site) {
		System.out.println("Data inserted successfully...");
		return siteRepo.save(site);
	}

	public String updateSite(Site site) {
		Integer siteId = site.getSiteId();
		int srNo = site.getSrNo();
		String siteName = site.getSiteName();
		String siteAddress = site.getSiteAddress();
		int projectId = site.getProjectId();
		int orgId = site.getOrgId();
		int branchId = site.getBranchId();
		int userId = site.getUserId();
		Date createdDate = site.getCreatedDate();
		int updatedBy = site.getUpdatedBy();
		Date updatedDate = site.getUpdatedDate();
		int isActive = site.getIsActive();

		String sql = "update site set site_id = ?, branch_id = ?, created_date = ?, is_active = ? , project_id = ?, "
				+ "site_address = ?, site_name = ?, sr_no = ?, updated_by = ?, updated_date = ?, user_id = ?"
				+ " where site_id= ? and org_id = ?";

		jdbcTemplate.update(sql, siteId, branchId, createdDate, isActive, projectId, siteAddress, siteName, srNo,
				updatedBy, updatedDate, userId, siteId, orgId);

		System.out.println("Record updated successfully..!");
		return "Record updated..!";
	}

	public Site updateDeleteSite(Site site) {
		Integer siteId = site.getSiteId();
		int updatedBy = site.getUpdatedBy();
		Date updatedDate = site.getUpdatedDate();
		int orgId = site.getOrgId();
		int isActive = site.getIsActive();

		String sql = "update site set is_active=?, updated_by=?,updated_date=? where site_id=? and org_id=?";
		jdbcTemplate.update(sql, isActive, updatedBy, updatedDate, siteId, orgId);
		System.out.println("Record updated");
		return siteRepo.save(site);
	}

	public List<Site> getAllSite() {
		return siteRepo.findAll();
	}

	public Site findSiteById(int siteId) {
		return siteRepo.findById(siteId).get();
	}

	public String deleteSiteById(int siteId) {
		siteRepo.deleteById(siteId);
		return "deleted";
	}
}
