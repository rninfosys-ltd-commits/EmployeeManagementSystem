package com.schoolapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.TransactionMasterDao;
import com.schoolapp.entity.TransactionMaster;

@Service
public class TransactionMasterService {
	@Autowired
	TransactionMasterDao TransactionMasterDao;

	public TransactionMaster saveTransactionMaster(TransactionMaster TransactionMaster) {
		return TransactionMasterDao.saveTransactionMaster(TransactionMaster);
	}

	public String getAllTransactionMaster(TransactionMaster TransactionMaster) {
		return TransactionMasterDao.getAllTransactionMaster(TransactionMaster);
	}

	public TransactionMaster findTransactionMasterById(int TransactionMasterId) {
		return TransactionMasterDao.findTransactionMasterById(TransactionMasterId);
	}

	public TransactionMaster updateTransactionMaster(TransactionMaster TransactionMaster) {
		TransactionMaster TransactionMasterObj = TransactionMasterDao
				.findTransactionMasterById(TransactionMaster.getTransactionMasterId());

		return TransactionMasterDao.saveTransactionMaster(TransactionMasterObj);
	}

	public String deleteTransactionMasterById(int TransactionMasterId) {
		TransactionMasterDao.deleteTransactionMasterById(TransactionMasterId);
		return "deleted";
	}
}
