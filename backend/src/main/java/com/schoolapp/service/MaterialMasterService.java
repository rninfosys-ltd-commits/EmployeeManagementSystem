package com.schoolapp.service;

import com.schoolapp.entity.MaterialMaster;
import java.util.List;

public interface MaterialMasterService {

    List<MaterialMaster> getAllActive();

    List<MaterialMaster> getAllActiveByOrg(int orgId);

    MaterialMaster save(MaterialMaster material);

    MaterialMaster update(Long id, MaterialMaster material);

    void delete(Long id);
}
