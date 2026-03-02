package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.StateDao;
import com.schoolapp.entity.State;

@Service
public class StateService {
	@Autowired
	StateDao stateDao;

	public State saveState(State state) {
		return stateDao.saveState(state);
	}

	public List<State> getAllState() {
		return stateDao.getAllState();
	}

	public State findStateById(int stateId) {
		return stateDao.findStateById(stateId);
	}

	public State updateState(State state) {

		Long stateId = state.getId(); // ✅ correct method

		State stateObj = stateDao.findStateById(stateId.intValue());

		stateObj.setName(state.getName()); // ✅ correct method

		return stateDao.saveState(stateObj);
	}

	public String deleteStateById(int stateId) {
		stateDao.deleteStateById(stateId);
		return "deleted";
	}
}
