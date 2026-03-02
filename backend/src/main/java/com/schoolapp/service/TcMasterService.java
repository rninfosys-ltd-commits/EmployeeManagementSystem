package com.schoolapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.entity.TcMaster;

@Service
public class TcMasterService {
	@Autowired
	com.schoolapp.dao.TcMasterDao TcMasterDao;

	public TcMaster saveTcMaster(TcMaster TcMaster) {
		return TcMasterDao.saveTcMaster(TcMaster);
	}

	public String getAllTcMaster(TcMaster TcMaster) {
		return TcMasterDao.getAllTcMaster(TcMaster);
	}

	public TcMaster findTcMasterById(int TcMasterId) {
		return TcMasterDao.findTcMasterById(TcMasterId);
	}

	public TcMaster updateTcMaster(TcMaster TcMaster) {
		return TcMasterDao.saveTcMaster(TcMaster);
	}

	public String deleteTcMasterById(int TcMasterId) {
		TcMasterDao.deleteTcMasterById(TcMasterId);
		return "deleted";
	}
}
