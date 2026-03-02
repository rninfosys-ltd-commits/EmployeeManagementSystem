package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.Resource;
import com.schoolapp.repository.ResourceRepo;

@Component
public class ResourceDao {

	@Autowired
	ResourceRepo resourceRepo;

	public Resource saveResource(Resource resource) {
		return resourceRepo.save(resource);
	}

	public List<Resource> getAllreso() {
		return resourceRepo.findAll();
	}

	public Resource findResoById(int resourceId) {
		return resourceRepo.findById(resourceId).get();
	}

	public String deleteResoById(int resource) {
		resourceRepo.deleteById(resource);
		return "deleted";
	}

}
