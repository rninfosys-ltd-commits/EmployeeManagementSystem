package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.ResourceDao;
import com.schoolapp.entity.Resource;

@Service
public class ResourceService {

	@Autowired
	ResourceDao resourceDao;

	public Resource saveResource(Resource resource) {
		return resourceDao.saveResource(resource);
	}

	public List<Resource> getAllReso() {
		return resourceDao.getAllreso();
	}

	public Resource findResoById(int resoId) {
		return resourceDao.findResoById(resoId);
	}

	public Resource updateReso(Resource resource) {
		Resource Res = resourceDao.findResoById(resource.getResourceId());
		Res.setResourceId(resource.getResourceId());
		Res.setResourceName(resource.getResourceName());
		Res.setResourceTpye(resource.getResourceTpye());
		return resourceDao.saveResource(Res);
	}

	public String deleteResoByID(int rId) {
		resourceDao.deleteResoById(rId);
		return "deleted";
	}

}
