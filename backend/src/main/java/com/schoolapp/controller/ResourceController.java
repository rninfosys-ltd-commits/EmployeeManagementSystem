package com.schoolapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.Resource;
import com.schoolapp.service.ResourceService;

@RestController
@RequestMapping("/resource")

public class ResourceController {

	@Autowired
	ResourceService resourceService;

	@PostMapping("/save")
	public String saveResource(@RequestBody Resource resource) {
		resourceService.saveResource(resource);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<Resource> getAllResource() {
		return resourceService.getAllReso();
	}

	@GetMapping("/get")
	public Resource findResourceById(@RequestBody Resource resource) {
		return resourceService.findResoById(resource.getResourceId());
		// return State;
	}

	@PutMapping("/update")
	public String updateResource(@RequestBody Resource resource) {
		resourceService.updateReso(resource);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteWingById(@RequestBody Resource resource) {
		int id = resource.getResourceId();

		if (id > 0) {
			resourceService.deleteResoByID(id);
			return "deleted....." + id;
		}

		return "Wrong ID" + id;
	}
}
