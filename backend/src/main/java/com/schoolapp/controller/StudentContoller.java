package com.schoolapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.Student;
import com.schoolapp.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentContoller {
	@Autowired
	StudentService studentservice;
	
	@GetMapping("/getAll")
	public List<Student> getAllStudents() {
        return studentservice.getAllStudents();
    }
    
	@PostMapping("/save")

    public Student registerStudent(@RequestBody Student student) {
        return studentservice.saveStudent(student);
    }


}
