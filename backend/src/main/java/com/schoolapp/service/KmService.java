package com.schoolapp.service;

import java.util.List;

import com.schoolapp.entity.KmDetails;

public interface KmService {

    KmDetails save(KmDetails kmDetails);

    List<KmDetails> findAll();

    // NEW: Get entries of a batch
    List<KmDetails> findByBatchNo(Long batchNo);

    // NEW: Update KM entry
    KmDetails update(Long id, KmDetails updated);

    // NEW: Delete entry
    void delete(Long id);
}
