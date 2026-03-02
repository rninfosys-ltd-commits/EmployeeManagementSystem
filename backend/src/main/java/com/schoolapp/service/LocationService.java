package com.schoolapp.service;

import org.springframework.stereotype.Service;

import com.schoolapp.dao.CityResponseDto;
import com.schoolapp.dao.TalukaResponseDto;
import com.schoolapp.entity.City;
import com.schoolapp.entity.District;
import com.schoolapp.entity.State;
import com.schoolapp.entity.Taluka;
import com.schoolapp.repository.CityRepository;
import com.schoolapp.repository.DistrictRepository;
import com.schoolapp.repository.StateRepository;
import com.schoolapp.repository.TalukaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final StateRepository stateRepo;
    private final DistrictRepository districtRepo;
    private final TalukaRepository talukaRepo;
    private final CityRepository cityRepo;

    public LocationService(
            StateRepository stateRepo,
            DistrictRepository districtRepo,
            TalukaRepository talukaRepo,
            CityRepository cityRepo) {
        this.stateRepo = stateRepo;
        this.districtRepo = districtRepo;
        this.talukaRepo = talukaRepo;
        this.cityRepo = cityRepo;
    }

    // ================= GET =================

    public List<State> getStates() {
        return stateRepo.findAll();
    }

    public List<District> getDistrictsByState(Long stateId) {
        return districtRepo.findByStateId(stateId);
    }

    public List<Taluka> getTalukasByDistrict(Long districtId) {
        return talukaRepo.findByDistrictId(districtId);
    }

    public List<City> getCitiesByTaluka(Long talukaId) {
        return cityRepo.findByTalukaId(talukaId);
    }

    // ================= GET ALL =================

    public List<District> getAllDistricts() {
        return districtRepo.findAll();
    }

    public List<Taluka> getAllTalukas() {
        return talukaRepo.findAll();
    }

    public List<City> getAllCities() {
        return cityRepo.findAll();
    }

    // ================= SAVE =================

    public State saveState(State state) {
        return stateRepo.save(state);
    }

    public District saveDistrict(Long stateId, District district) {
        State state = stateRepo.findById(stateId)
                .orElseThrow(() -> new RuntimeException("State not found"));

        district.setState(state);
        return districtRepo.save(district);
    }

    // ✅ FIXED (ID BASED – NO RELATION)
    public Taluka saveTaluka(Long districtId, Taluka taluka) {

        District district = districtRepo.findById(districtId)
                .orElseThrow(() -> new RuntimeException("District not found"));

        taluka.setDistrictId(district.getId());
        taluka.setStateId(district.getState().getId());

        return talukaRepo.save(taluka);
    }

    public City saveCity(Long talukaId, City city) {

        Taluka taluka = talukaRepo.findById(talukaId)
                .orElseThrow(() -> new RuntimeException("Taluka not found"));

        District district = districtRepo.findById(taluka.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found"));

        State state = district.getState(); // ✅ SIMPLE + SAFE

        city.setTaluka(taluka);
        city.setDistrict(district);
        city.setState(state);

        return cityRepo.save(city);
    }

    // ================= CITY DTO METHODS =================
    // ✅ FIXED TO ID-BASED (NO getTaluka(), getDistrict())

    private CityResponseDto mapCityToDto(City city) {

        CityResponseDto dto = new CityResponseDto();

        // City
        dto.setCityId(city.getId());
        dto.setCityName(city.getName());

        // Taluka
        dto.setTalukaId(city.getTaluka().getId());
        dto.setTalukaName(city.getTaluka().getName());

        // District
        dto.setDistrictId(city.getDistrict().getId());
        dto.setDistrictName(city.getDistrict().getName());

        // State
        dto.setStateId(city.getState().getId());
        dto.setStateName(city.getState().getName());

        return dto;
    }

    public List<CityResponseDto> getAllCitiesDto() {
        return cityRepo.findAll()
                .stream()
                .map(this::mapCityToDto)
                .collect(Collectors.toList());
    }

    public List<CityResponseDto> getCitiesByTalukaDto(Long talukaId) {
        return cityRepo.findByTalukaId(talukaId)
                .stream()
                .map(this::mapCityToDto)
                .collect(Collectors.toList());
    }

    public List<TalukaResponseDto> getTalukasByDistrictDto(Long districtId) {

        List<Taluka> talukas = talukaRepo.findByDistrictId(districtId);

        District district = districtRepo.findById(districtId)
                .orElseThrow(() -> new RuntimeException("District not found"));

        State state = stateRepo.findById(district.getState().getId())
                .orElseThrow(() -> new RuntimeException("State not found"));

        return talukas.stream().map(t -> {
            TalukaResponseDto dto = new TalukaResponseDto();

            dto.setId(t.getId());
            dto.setName(t.getName());

            dto.setDistrictId(district.getId());
            dto.setDistrictName(district.getName());

            dto.setStateId(state.getId());
            dto.setStateName(state.getName());

            return dto;
        }).collect(Collectors.toList());
    }

}
