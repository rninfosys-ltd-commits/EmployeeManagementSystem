package com.schoolapp.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.Floor;
import com.schoolapp.service.FloorService;

@RestController
@RequestMapping("/floor")

public class FloorController {
	@Autowired
	FloorService floorService;

	@PostMapping("/save")
	public String saveFloor(@RequestBody Floor floor) throws ClassNotFoundException, SQLException {
		floorService.saveFloor(floor);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public String getAllFloor() throws ClassNotFoundException, SQLException {
		return floorService.getAllFloor();
	}

	@GetMapping("/get")
	public Floor findFloorById(@RequestBody Floor floor) {
		return floorService.findFloorById(floor.getFloorId());

	}

	@PostMapping("/getFloorSite")
	public String getFloorSite(@RequestBody Floor floor) throws Exception {
		return floorService.getFloorSite(floor);
	}

	@PostMapping("/getFloorWing")
	public String getFloorWing(@RequestBody Floor floor) throws Exception {
		return floorService.getFloorWing(floor);
	}

	@PutMapping("/update")
	public String updateFloor(@RequestBody Floor floor) throws ClassNotFoundException, SQLException {
		floorService.updateFloor(floor);
		return "Record Updated..";
	}

	@PutMapping("/updateDeleteFloor")
	public String updateDeleteFloor(@RequestBody Floor floor) throws ClassNotFoundException, SQLException {

		floorService.updateDeleteFloor(floor);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deletedFloorById(@RequestBody Floor floor) {
		int id = floor.getFloorId();
		if (id > 0) {
			floorService.deleteFloorById(id);
			return "deleted.." + id;
		}
		return "wrong Id" + id;
	}
}
