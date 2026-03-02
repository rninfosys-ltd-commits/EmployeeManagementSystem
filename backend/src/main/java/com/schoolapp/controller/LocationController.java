package com.schoolapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.CityResponseDto;
import com.schoolapp.dao.TalukaResponseDto;
import com.schoolapp.entity.City;
import com.schoolapp.entity.District;
import com.schoolapp.entity.State;
import com.schoolapp.entity.Taluka;
import com.schoolapp.service.LocationService;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // ================= GET =================

    @GetMapping("/states")
    public List<State> getStates() {
        return locationService.getStates();
    }

    @GetMapping("/districts/{stateId}")
    public List<District> getDistrictsByState(@PathVariable Long stateId) {
        return locationService.getDistrictsByState(stateId);
    }

    @GetMapping("/talukas/{districtId}")
    public List<Taluka> getTalukasByDistrict(@PathVariable Long districtId) {
        return locationService.getTalukasByDistrict(districtId);
    }

    @GetMapping("/cities/{talukaId}")
    public List<City> getCitiesByTaluka(@PathVariable Long talukaId) {
        return locationService.getCitiesByTaluka(talukaId);
    }

    // ================= GET ALL =================

    @GetMapping("/districts")
    public List<District> getAllDistricts() {
        return locationService.getAllDistricts();
    }

    @GetMapping("/talukas")
    public List<Taluka> getAllTalukas() {
        return locationService.getAllTalukas();
    }

    @GetMapping("/cities")
    public List<City> getAllCities() {
        return locationService.getAllCities();
    }

    // ================= DTO GET =================

    @GetMapping("/cities/by-taluka/{talukaId}")
    public List<CityResponseDto> getCitiesByTalukaDto(@PathVariable Long talukaId) {
        return locationService.getCitiesByTalukaDto(talukaId);
    }

    @GetMapping("/cities/dto")
    public List<CityResponseDto> getAllCitiesDto() {
        return locationService.getAllCitiesDto();
    }

    @GetMapping("/talukas/dto/{districtId}")
    public List<TalukaResponseDto> getTalukasByDistrictDto(@PathVariable Long districtId) {
        return locationService.getTalukasByDistrictDto(districtId);
    }

    // ================= SAVE =================

    @PostMapping("/states")
    public State saveState(@RequestBody State state) {
        return locationService.saveState(state);
    }

    @PostMapping("/districts/{stateId}")
    public District saveDistrict(@PathVariable Long stateId, @RequestBody District district) {
        return locationService.saveDistrict(stateId, district);
    }

    @PostMapping("/talukas/{districtId}")
    public Taluka saveTaluka(@PathVariable Long districtId, @RequestBody Taluka taluka) {
        return locationService.saveTaluka(districtId, taluka);
    }

    @PostMapping("/cities/{talukaId}")
    public City saveCity(@PathVariable Long talukaId, @RequestBody City city) {
        return locationService.saveCity(talukaId, city);
    }
}
