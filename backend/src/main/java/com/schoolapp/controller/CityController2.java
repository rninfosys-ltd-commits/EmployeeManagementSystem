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

import com.schoolapp.entity.City2;
import com.schoolapp.service.CityService2;

@RestController
@RequestMapping("/city2")

public class CityController2 {

	@Autowired
	CityService2 cityservice2;

	@PostMapping("/save")
	public String saveCity(@RequestBody City2 city) {
		cityservice2.saveCity(city);
		return "Record Saved Successfully....";
	}

	@GetMapping("/getAll")
	public List<City2> getAllCity() {
		return cityservice2.getAllCity();
	}

	@GetMapping("/get")
	public City2 findCityById(@RequestBody City2 city) {

		return cityservice2.findCityById(city.getCityId());

	}

	@PutMapping("/update")
	public String updateCity(@RequestBody City2 city) {
		cityservice2.updateCity(city);
		return "Record Updated Succussfully....";
	}

	@DeleteMapping("/delete")
	public String deleteCityById(@RequestBody City2 city) {
		int id = city.getCityId();
		if (id < 0) {
			cityservice2.deleteCityById(id);
			return "deleted..." + id;
		}
		return "Wrong Id " + id;
	}

}
