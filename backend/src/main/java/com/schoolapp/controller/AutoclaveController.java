package com.schoolapp.controller;

//package com.crmemp.controller;

//import com.crmemp.dto.AutoclaveDTO;
//import com.crmemp.service.AutoclaveService;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.AutoclaveDTO;
import com.schoolapp.service.AutoclaveService;

import java.util.List;

@RestController

@RequestMapping("/api/autoclave")

public class AutoclaveController {

    private final AutoclaveService service;

    public AutoclaveController(AutoclaveService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public AutoclaveDTO save(@RequestBody AutoclaveDTO dto) {
        return service.save(dto);
    }

    @GetMapping
    public List<AutoclaveDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AutoclaveDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public AutoclaveDTO update(
            @PathVariable Long id,
            @RequestBody AutoclaveDTO dto) {

        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
