package com.schoolapp.service;

import java.util.List;

import com.schoolapp.dao.AutoclaveDTO;

public interface AutoclaveService {

    AutoclaveDTO save(AutoclaveDTO dto);

    List<AutoclaveDTO> getAll();

    AutoclaveDTO getById(Long id);

    void delete(Long id);

    AutoclaveDTO update(Long id, AutoclaveDTO dto);

}
