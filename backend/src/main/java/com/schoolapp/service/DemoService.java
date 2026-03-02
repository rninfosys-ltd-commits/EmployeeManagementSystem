package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.dao.DemoDao;
import com.schoolapp.entity.DemoEntity;

@Service
public class DemoService {
	@Autowired
	
	DemoDao demodao;

	public DemoEntity save(DemoEntity de) {
		return demodao.save(de);
	}
	
	public List<DemoEntity> getAll( ){
		return demodao.getAll( );
	}
	
	public   DemoEntity  getRecordById(int de) {
		return demodao.findById(de);
	}
}
