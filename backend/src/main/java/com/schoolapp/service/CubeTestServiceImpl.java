package com.schoolapp.service;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
//import com.Crmemp.entity.CubeTestEntity;
//import com.Crmemp.repository.CubeTestRepository;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

import com.schoolapp.entity.CubeTestEntity;
import com.schoolapp.repository.CubeTestRepository;

@Service
public class CubeTestServiceImpl implements CubeTestService {

    private final CubeTestRepository repo;

    // ✅ ADD THIS CONSTRUCTOR
    public CubeTestServiceImpl(CubeTestRepository repo) {
        this.repo = repo;
    }

    @Override
    public CubeTestEntity save(CubeTestEntity cubeTest) {

        cubeTest.setCreatedDate(new Date());
        cubeTest.setIsActive(1);

        // 🔥 ADD THIS
        cubeTest.setApprovalStage("NONE");

        return repo.save(cubeTest);
    }

    @Override
    public CubeTestEntity update(Long id, CubeTestEntity cubeTest) {

        CubeTestEntity db = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cube Test not found"));

        db.setBatchNo(cubeTest.getBatchNo());
        db.setCastDate(cubeTest.getCastDate());
        db.setTestingDate(cubeTest.getTestingDate());
        db.setShift(cubeTest.getShift());

        db.setCubeDimensionImmediate(cubeTest.getCubeDimensionImmediate());
        db.setCubeDimensionOverDry(cubeTest.getCubeDimensionOverDry());

        db.setWeightImmediateKg(cubeTest.getWeightImmediateKg());
        db.setWeightOverDryKg(cubeTest.getWeightOverDryKg());
        db.setWeightWithMoistureKg(cubeTest.getWeightWithMoistureKg());

        db.setLoadOverDryTonn(cubeTest.getLoadOverDryTonn());
        db.setLoadMoistureTonn(cubeTest.getLoadMoistureTonn());

        db.setCompStrengthOverDry(cubeTest.getCompStrengthOverDry());
        db.setCompStrengthMoisture(cubeTest.getCompStrengthMoisture());

        db.setDensityKgM3(cubeTest.getDensityKgM3());

        db.setUpdatedBy(cubeTest.getUpdatedBy());
        db.setUpdatedDate(new Date());
        db.setIsActive(cubeTest.getIsActive());

        return repo.save(db);
    }

    @Override
    public List<CubeTestEntity> findAll() {
        return repo.findAll();
    }

    @Override
    public CubeTestEntity findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cube Test not found"));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // @Override
    // public CubeTestEntity approve(Long cubeId) {
    //
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
    // String role = user.getRole();
    //
    // CubeTestEntity entry = repo.findById(cubeId)
    // .orElseThrow(() -> new RuntimeException("Cube Test not found"));
    //
    // String stage = entry.getApprovalStage();
    //
    // if(stage == null || stage.isEmpty()){
    // stage = "NONE";
    // }
    //
    //
    //
    //
    // switch (stage) {
    //
    // case "NONE":
    // if (!"ROLE_DIRECTOR".equals(role))
    // throw new RuntimeException("Only L1 can approve");
    // entry.setApprovedByL1(user.getUsername());
    // entry.setApprovalStage("L1");
    // break;
    //
    // case "L1":
    // if (!"ROLE_MANAGER".equals(role))
    // throw new RuntimeException("Only L2 can approve");
    // entry.setApprovedByL2(user.getUsername());
    // entry.setApprovalStage("L2");
    // break;
    //
    // case "L2":
    // if (!"ROLE_SUPERVISOR".equals(role))
    // throw new RuntimeException("Only L3 can approve");
    // entry.setApprovedByL3(user.getUsername());
    // entry.setApprovalStage("L3");
    // break;
    //
    // default:
    // throw new RuntimeException("Already approved");
    // }
    //
    // return repo.save(entry);
    // }
    @Override
    public CubeTestEntity approve(Long id, Long userId, String role) {

        CubeTestEntity entry = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cube Test not found"));

        String stage = entry.getApprovalStage();
        if (stage == null)
            stage = "NONE";

        switch (stage) {
            case "NONE":
                entry.setApprovedByL1(String.valueOf(userId));
                entry.setApprovalStage("L1");
                break;

            case "L1":
                entry.setApprovedByL2(String.valueOf(userId));
                entry.setApprovalStage("L2");
                break;

            case "L2":
                entry.setApprovedByL3(String.valueOf(userId));
                entry.setApprovalStage("L3");
                break;

            default:
                throw new RuntimeException("Already approved");
        }

        return repo.save(entry);
    }

    @Override
    public CubeTestEntity reject(Long cubeId, String reason, Long userId, String role) {

        CubeTestEntity entry = repo.findById(cubeId)
                .orElseThrow(() -> new RuntimeException("Cube Test not found"));

        entry.setApprovalStage("NONE");
        entry.setApprovedByL1(null);
        entry.setApprovedByL2(null);
        entry.setApprovedByL3(null);
        entry.setRejectedBy(String.valueOf(userId));
        entry.setRejectReason(reason);

        return repo.save(entry);
    }

}
