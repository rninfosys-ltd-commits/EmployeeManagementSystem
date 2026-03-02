package com.schoolapp.controller;
//package com.yourapp.blockseparating.controller;

//import com.yourapp.blockseparating.dto.BlockSeparatingDTO;
//import com.yourapp.blockseparating.service.BlockSeparatingService;
import org.springframework.web.bind.annotation.*;

import com.schoolapp.dao.BlockSeparatingDTO;
import com.schoolapp.service.BlockSeparatingService;

import java.util.List;

@RestController

@RequestMapping("/api/block-separating")

public class BlockSeparatingController {

    private final BlockSeparatingService service;

    public BlockSeparatingController(BlockSeparatingService service) {
        this.service = service;
    }

    @PostMapping
    public BlockSeparatingDTO create(@RequestBody BlockSeparatingDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public BlockSeparatingDTO update(
            @PathVariable Long id,
            @RequestBody BlockSeparatingDTO dto) {
        return service.update(id, dto);
    }

    @GetMapping
    public List<BlockSeparatingDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public BlockSeparatingDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
