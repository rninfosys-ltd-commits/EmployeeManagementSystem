package com.schoolapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolapp.entity.KmDetails;
import com.schoolapp.service.KmService;

//import com.Crmemp.entity.KmDetails;
//import com.Crmemp.services.KmService;

@RestController
@RequestMapping("/api/km")

public class KmController {

    @Autowired
    private KmService kmService;

    @PostMapping
    public KmDetails saveKm(@RequestBody KmDetails kmDetails) {
        return kmService.save(kmDetails);
    }

    @GetMapping("/all")
    public List<KmDetails> findAll() {
        return kmService.findAll();
    }

}
