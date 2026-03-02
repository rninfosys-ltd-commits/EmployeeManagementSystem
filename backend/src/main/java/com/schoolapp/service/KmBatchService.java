package com.schoolapp.service;

import java.util.List;

import com.schoolapp.entity.KmBatch;
import com.schoolapp.entity.KmDetails;

public interface KmBatchService {

    KmBatch createBatch(KmBatch batch);

    KmDetails addEntry(Long batchNo, KmDetails entry);

    List<KmBatch> getAllBatches();

    List<KmDetails> getEntries(Long batchNo);

    KmBatch approveBatch(Long batchNo, Long userId, String role);

    KmBatch rejectBatch(Long batchNo, Long userId, String role, String reason);

    void deleteBatch(Long batchNo);

}
