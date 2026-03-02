package com.schoolapp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolapp.entity.State;
import com.schoolapp.repository.StateRepo;

@Component
public class StateDao {
	
	@Autowired
	StateRepo stateRepo;
	
	public State saveState(State state) {
		return stateRepo.save(state);
	}
	
	public List<State> getAllState(){
		return stateRepo.findAll();
	}
	
	public State findStateById(int stateId) {
		return stateRepo.findById(stateId).get();
	}
	
	public String deleteStateById(int stateId) {
		stateRepo.deleteById(stateId);
		return "deleted";
	}
	
	
}
