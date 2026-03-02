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

import com.schoolapp.entity.Site;
import com.schoolapp.service.SiteService;

@RestController
@RequestMapping("/site")

public class SiteController {
	@Autowired
	SiteService siteService;

	@PostMapping("/save")
	public String saveSite(@RequestBody Site site) throws ClassNotFoundException, SQLException {
		siteService.saveSite(site);
		return "Record save successfully";
	}

	// getall
	@GetMapping("/getAll")
	public List<Site> getAllSite() {
		return siteService.getAllSite();
	}

	//
	@GetMapping("/get")
	public Site findSiteById(@RequestBody Site site) {

		return siteService.findStateById(site.getSiteId());
		// return State;
	}

	@PutMapping("/updateDeleteSite")
	public String updateDeleteSite(@RequestBody Site site)
			throws ClassNotFoundException, SQLException {
		siteService.updateDeleteSite(site);
		return "Record Updated.....";
	}

	@PutMapping("/update")
	public String updateUser(@RequestBody Site site) throws ClassNotFoundException, SQLException {
		siteService.updateSite(site);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteSiteById(@RequestBody Site site) {
		int id = site.getSiteId();

		if (id > 0) {
			siteService.deleteSiteById(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}
