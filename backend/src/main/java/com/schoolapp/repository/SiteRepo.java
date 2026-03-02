package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Site;

public interface SiteRepo extends JpaRepository<Site, Integer>{

}
