package com.schoolapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolapp.entity.KmDetails;
import com.schoolapp.repository.KmRepository;

import java.util.List;

@Service
public class KmServiceImpl implements KmService {

    @Autowired
    private KmRepository kmRepository;

    @Override
    public KmDetails save(KmDetails kmDetails) {
        return kmRepository.save(kmDetails);
    }

    @Override
    public List<KmDetails> findAll() {
        return kmRepository.findAll();
    }

    @Override
    public List<KmDetails> findByBatchNo(Long batchNo) {
        return kmRepository.findByBatch_KmBatchNo(batchNo);
    }

    @Override
    public KmDetails update(Long id, KmDetails updated) {
        KmDetails existing = kmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("KM entry not found"));

        existing.setSalesperson(updated.getSalesperson());
        existing.setStartKm(updated.getStartKm());
        existing.setEndKm(updated.getEndKm());
        existing.setVisitedPlace(updated.getVisitedPlace());
        existing.setTrnDate(updated.getTrnDate());
        existing.setFilePath(updated.getFilePath());

        return kmRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        kmRepository.deleteById(id);
    }
}
