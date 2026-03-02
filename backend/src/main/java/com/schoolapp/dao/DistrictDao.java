package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.District;
import com.schoolapp.repository.DistrictRepo;

@Component
public class DistrictDao {
	@Autowired
	DistrictRepo districtRepo;
	
	public District saveDistrict(District district) {
		return districtRepo.save(district);
	}
	
	public List<District> getAllDistrict(){
		return districtRepo.findAll();
	}
	
	public District findDistrictById(int DistrictId) {
		return districtRepo.findById(DistrictId).get();
	}
	
	public String deleteDistrictById(int DistrictId) {
		districtRepo.deleteById(DistrictId);
		return "deleted";
	}

}
