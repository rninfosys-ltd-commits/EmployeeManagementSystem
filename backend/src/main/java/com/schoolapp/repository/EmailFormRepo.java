package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.EmailForm;

public interface EmailFormRepo extends JpaRepository<EmailForm, Integer> {

}
