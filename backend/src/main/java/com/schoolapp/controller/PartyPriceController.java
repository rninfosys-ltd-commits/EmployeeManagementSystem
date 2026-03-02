package com.schoolapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.PartyPriceDto;
import com.schoolapp.service.PartyPriceService;

import java.util.List;

@RestController
@RequestMapping("/api/party-prices")

public class PartyPriceController {

    private final PartyPriceService service;

    public PartyPriceController(PartyPriceService service) {
        this.service = service;
    }

    // CREATE / UPDATE
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public PartyPriceDto save(@RequestBody PartyPriceDto dto) {
        return service.save(dto);
    }

    // GET ALL
    @GetMapping
    public List<PartyPriceDto> getAll() {
        return service.getAll();
    }

    // GET BY PARTY
    @GetMapping("/party/{partyId}")
    public List<PartyPriceDto> getByParty(@PathVariable Long partyId) {
        return service.getByParty(partyId);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY_OWNER')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
