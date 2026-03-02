package com.schoolapp.service;

import com.schoolapp.dao.RejectionDataDTO;

public interface RejectionDataService {

    Object create(RejectionDataDTO dto);

    Object getAll();

    Object getById(Long id);

    Object update(Long id, RejectionDataDTO dto);

    Object delete(Long id);
}
