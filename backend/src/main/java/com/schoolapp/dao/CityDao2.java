package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.City2;
import com.schoolapp.repository.CityRepo2;

@Component
	public class CityDao2 {
		@Autowired
	CityRepo2 cityrepo2;
		
		public City2 saveCity(City2 city)
		{
			return cityrepo2.save(city);
		}
		
		public List<City2> getAllCity(){
			return cityrepo2.findAll();
			
		}
		
		public City2 findCityById(int cityid) {
			return cityrepo2.findById(cityid).get();
		}
		public String deleteCityById(int cityid) {
			cityrepo2.deleteById(cityid);
			return "delted";
		}
	}
