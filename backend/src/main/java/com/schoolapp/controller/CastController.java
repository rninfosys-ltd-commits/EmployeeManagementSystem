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

import com.schoolapp.entity.Cast;
import com.schoolapp.service.CastService;

@RestController
@RequestMapping("/cast")

public class CastController {
	@Autowired
	CastService CastService;

	@PostMapping("/save")
	public String saveCast(@RequestBody Cast Cast) {
		CastService.saveCast(Cast);
		return "Record save successfully";
	}

	@GetMapping("/getAll")
	public List<Cast> getAllCast() {

		return CastService.getAllCast();
	}

	@GetMapping("/get")
	public Cast findCastById(@RequestBody Cast Cast) {

		return CastService.findCastById(Cast.getCastId());
		// return State;
	}

	@PutMapping("/update")
	public String updateCast(@RequestBody Cast Cast) {
		CastService.updateCast(Cast);
		return "Record Updated.....";
	}

	@DeleteMapping("/delete")
	public String deleteCastById(@RequestBody Cast Cast) {
		int id = Cast.getCastId();
		if (id > 0) {
			CastService.deleteCastById(id);
			return "deleted..." + id;
		}

		return "Wrong Id" + id;
	}
}
