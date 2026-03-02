package com.schoolapp.service;

import org.springframework.stereotype.Service;

import com.schoolapp.dao.AutoclaveDTO;
import com.schoolapp.dao.AutoclaveWagonDTO;
import com.schoolapp.entity.AutoclaveCycle;
import com.schoolapp.entity.AutoclaveWagon;
import com.schoolapp.repository.AutoclaveRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AutoclaveServiceImpl implements AutoclaveService {

    private final AutoclaveRepository repo;

    public AutoclaveServiceImpl(AutoclaveRepository repo) {
        this.repo = repo;
    }

    @Override
    public AutoclaveDTO save(AutoclaveDTO dto) {

        AutoclaveCycle a = new AutoclaveCycle();
        a.setAutoclaveNo(generateAutoclaveNo());

        a.setRunNo(dto.runNo);
        a.setStartedAt(dto.startedAt);
        a.setStartedDate(dto.startedDate);
        a.setCompletedAt(dto.completedAt);
        a.setCompletedDate(dto.completedDate);
        a.setRemarks(dto.remarks);

        a.setUserId(dto.userId);
        a.setBranchId(dto.branchId);
        a.setOrgId(dto.orgId);
        a.setCreatedDate(new Date());
        a.setIsActive(1);

        List<AutoclaveWagon> wagons = new ArrayList<>();

        for (AutoclaveWagonDTO w : dto.wagons) {
            AutoclaveWagon aw = new AutoclaveWagon();
            aw.setEBatch(w.eBatch);
            aw.setESize(w.eSize);
            aw.setMBatch(w.mBatch);
            aw.setMSize(w.mSize);
            aw.setWBatch(w.wBatch);
            aw.setWSize(w.wSize);

            aw.setUserId(dto.userId);
            aw.setBranchId(dto.branchId);
            aw.setOrgId(dto.orgId);
            aw.setCreatedDate(new Date());
            aw.setIsActive(1);

            aw.setAutoclave(a);
            wagons.add(aw);
        }

        a.setWagons(wagons);

        repo.save(a);
        dto.id = a.getId();
        return dto;
    }

    @Override
    public List<AutoclaveDTO> getAll() {

        return repo.findAll().stream().map(a -> {

            AutoclaveDTO d = new AutoclaveDTO();

            d.id = a.getId();
            d.autoclaveNo = a.getAutoclaveNo();
            d.runNo = a.getRunNo();
            d.startedAt = a.getStartedAt();
            d.startedDate = a.getStartedDate();
            d.completedAt = a.getCompletedAt();
            d.completedDate = a.getCompletedDate();
            d.remarks = a.getRemarks();

            // 🔥 THIS IS THE FIX
            if (a.getWagons() != null) {
                d.wagons = a.getWagons().stream().map(w -> {
                    AutoclaveWagonDTO wd = new AutoclaveWagonDTO();
                    wd.eBatch = w.getEBatch();
                    wd.eSize = w.getESize();
                    wd.mBatch = w.getMBatch();
                    wd.mSize = w.getMSize();
                    wd.wBatch = w.getWBatch();
                    wd.wSize = w.getWSize();
                    return wd;
                }).collect(Collectors.toList());
            } else {
                d.wagons = new ArrayList<>();
            }

            return d;

        }).collect(Collectors.toList());
    }

    @Override
    public AutoclaveDTO getById(Long id) {
        AutoclaveCycle a = repo.findById(id).orElseThrow();
        AutoclaveDTO d = new AutoclaveDTO();
        d.id = a.getId();
        d.autoclaveNo = a.getAutoclaveNo();
        d.runNo = a.getRunNo();
        d.startedAt = a.getStartedAt();
        d.startedDate = a.getStartedDate();
        d.completedAt = a.getCompletedAt();
        d.completedDate = a.getCompletedDate();
        d.remarks = a.getRemarks();

        d.wagons = a.getWagons().stream().map(w -> {
            AutoclaveWagonDTO wd = new AutoclaveWagonDTO();
            wd.eBatch = w.getEBatch();
            wd.eSize = w.getESize();
            wd.mBatch = w.getMBatch();
            wd.mSize = w.getMSize();
            wd.wBatch = w.getWBatch();
            wd.wSize = w.getWSize();
            return wd;
        }).collect(Collectors.toList());

        return d;
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public AutoclaveDTO update(Long id, AutoclaveDTO dto) {

        AutoclaveCycle a = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Autoclave not found"));

        // ✅ DO NOT touch autoclaveNo
        a.setRunNo(dto.runNo);
        a.setStartedAt(dto.startedAt);
        a.setStartedDate(dto.startedDate);
        a.setCompletedAt(dto.completedAt);
        a.setCompletedDate(dto.completedDate);
        a.setRemarks(dto.remarks);

        a.setUpdatedDate(new Date());
        a.setUpdatedBy(dto.userId);

        // ✅ orphanRemoval-safe update
        List<AutoclaveWagon> existingWagons = a.getWagons();
        existingWagons.clear();

        if (dto.wagons != null) {
            for (AutoclaveWagonDTO w : dto.wagons) {

                AutoclaveWagon aw = new AutoclaveWagon();
                aw.setEBatch(w.eBatch);
                aw.setESize(w.eSize);
                aw.setMBatch(w.mBatch);
                aw.setMSize(w.mSize);
                aw.setWBatch(w.wBatch);
                aw.setWSize(w.wSize);

                aw.setUserId(dto.userId);
                aw.setBranchId(dto.branchId);
                aw.setOrgId(dto.orgId);
                aw.setCreatedDate(new Date());
                aw.setIsActive(1);

                aw.setAutoclave(a); // owning side
                existingWagons.add(aw);
            }
        }

        repo.save(a);

        // return updated autoclave number safely
        dto.autoclaveNo = a.getAutoclaveNo();
        return dto;
    }

    private String generateAutoclaveNo() {

        String lastNo = repo.findLastAutoclaveNo();

        if (lastNo == null) {
            return "AUTO-0001";
        }

        int num = Integer.parseInt(lastNo.replace("AUTO-", ""));
        num++;

        return String.format("AUTO-%04d", num);
    }

}
