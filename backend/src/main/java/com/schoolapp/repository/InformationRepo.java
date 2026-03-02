package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Information;

public interface InformationRepo extends JpaRepository< Information , Integer>{

}
