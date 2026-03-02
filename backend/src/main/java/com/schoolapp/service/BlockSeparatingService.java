package com.schoolapp.service;

import java.util.List;

import com.schoolapp.dao.BlockSeparatingDTO;

public interface BlockSeparatingService {

    BlockSeparatingDTO create(BlockSeparatingDTO dto);

    BlockSeparatingDTO update(Long id, BlockSeparatingDTO dto);

    List<BlockSeparatingDTO> getAll();

    BlockSeparatingDTO getById(Long id);

    void delete(Long id);
}
