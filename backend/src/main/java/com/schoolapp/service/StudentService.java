package com.schoolapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.entity.Student;
import com.schoolapp.repository.StudentRepo;

	@Service 
	public class StudentService {
	@Autowired
	StudentRepo studentrepo;
	
	public List<Student> getAllStudents() {
	    return studentrepo.findAll();
	}
	
	public Student saveStudent(Student student) {
	    return studentrepo.save(student);
	}


}
