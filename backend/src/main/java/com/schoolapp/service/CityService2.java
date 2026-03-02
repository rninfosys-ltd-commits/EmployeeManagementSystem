package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.CityDao2;
import com.schoolapp.entity.City2;

@Service
public class CityService2 {
	@Autowired
	CityDao2 citydao2;

	public City2 saveCity(City2 city) {
		return citydao2.saveCity(city);

	}

	public List<City2> getAllCity() {
		return citydao2.getAllCity();
	}

	public City2 updateCity(City2 cityId) {
		City2 city = citydao2.findCityById(cityId.getCityId());
		city.setCityId(cityId.getCityId());
		city.setCityName(cityId.getCityName());
		city.setDistId(cityId.getDistId());
		city.setStateId(cityId.getStateId());
		return citydao2.saveCity(city);
	}

	public String deleteCityById(int cityId) {
		citydao2.deleteCityById(cityId);
		return "deleted";

	}

	public City2 findCityById(int cityId) {
		return citydao2.findCityById(cityId);
	}

}
